/**
 * Created by oshchepkovayu on 17.08.16.
 */
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;


import java.awt.image.WritableRaster;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Region_borders {

    static List<Integer> index_points_cluster = new ArrayList<Integer>();

    static int[] ResultArray;
    static int limit_for;

    static int count_iteration_in_cl;
    static int all_count_iteration_in_cl;
    static BufferedImage image = null;
    static int rand_value = 0;


    static int h;
    static int w;

    static int count_center_cluster = 0;
    static int value_center_cluster = 0;


    static WritableRaster r = null;

    public static void main(String[] args) throws IOException {

        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

        r = Region_borders.getRaster_input();
        int count_iterations_in_cluster = 0;
        int count_break_while = 0;


        rand_value = get_random_index();
        count_iteration_in_cl = 1;
        value_center_cluster = ResultArray[rand_value];
        count_center_cluster++;
        index_points_cluster.add(value_center_cluster);




        IntStream.rangeClosed(1,((w - 4) * (h - 5))).parallel().
                forEach((i)->{
                    ResultArray[i] = ResultArray[i-1]<ResultArray[i+1]+limit_for?255:ResultArray[i];
                    ResultArray[i-1] = ResultArray[i]==255?255:ResultArray[i-1];
//                    ResultArray[i+1] = ResultArray[i]==255?0:ResultArray[i+1];

                    count_iteration_in_cl++;

                    if (count_iteration_in_cl>=(w - 4) * (h - 5)) {
                        return;
                    }
                });







            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
            assert image != null;
            image.setData(r);
            ImageIO.write(image, "tiff", new File("output/result_" + limit_for + ".tiff"));

            System.out.print("Время старта обработки:" + timeFormat.format(StartPro)
                    + System.getProperty("line.separator") +
                    "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));


    }


    public static void reseach_RG_for_stream(int i) {

        ResultArray[i] = ResultArray[i-1]<ResultArray[i+1]+limit_for?255:ResultArray[i];
        ResultArray[i-1] = ResultArray[i]==255?0:ResultArray[i-1];
        ResultArray[i+1] = ResultArray[i]==255?0:ResultArray[i+1];

        count_iteration_in_cl++;
    }

    public static int get_random_index()
    {
        int index_while = 0;
        int rand = -1;
        while (index_while<w)
        {
            rand = new Random().nextInt((w - 4) * (h));
            rand = rand <(h * 3)?(h * 3):rand;
            if (index_points_cluster.indexOf(ResultArray[rand])==-1 & ResultArray[rand]!=0)
            {
                index_while = w;
            }
            else{index_while++;}
        }

        return rand;
    }

    public static WritableRaster getRaster_input() {
        limit_for = 5;
        File TiffPath = new File("input");
        File[] listFile = TiffPath.listFiles();
        WritableRaster r = null;

        for (int i = 0; i < listFile.length; i++) {

            InputStream is = null;
            Rectangle sourceRegion = null;// The region you want to extract

            try {
                is = new FileInputStream("input/" + listFile[i].getName());

                ImageInputStream stream = ImageIO.createImageInputStream(is); // File or input stream

                Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);


                if (readers.hasNext()) {
                    ImageReader reader = readers.next();

                    reader.setInput(stream);
                    h = reader.getHeight(reader.getMinIndex());
                    w = reader.getWidth(reader.getMinIndex());

                    ResultArray = new int[(h - 1) * (w - 1)];

                    ImageReadParam param = reader.getDefaultReadParam();

                    sourceRegion = new Rectangle(0, 0, w, h);
                    param.setSourceRegion(sourceRegion); // Set region

                    image = reader.read(0, param); // Will read only the region specified

                    param = null;
                    sourceRegion = null;
                    reader = null;

                    r = (WritableRaster) image.getData();
                    r.getPixels(1, 1, w - 1, h - 1, ResultArray);
                    /************************/


                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r;
    }
}



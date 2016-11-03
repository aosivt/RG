/**
 * Created by oshchepkovayu on 12.08.16.
 */


import org.apache.commons.lang3.ArrayUtils;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class RegionGrowingFour {

    static List<Integer> index_points_cluster = new ArrayList<Integer>();
    static List<Integer> test_index_points_cluster = new ArrayList<Integer>();

    static int[] ResultArray;
    static int limit_for;

    static int count_iteration_in_cl;
    static BufferedImage image = null;
    static int rand_value = 0;


    static int h;
    static int w;

    static int count_center_cluster = 0;
    static int value_center_cluster = 0;
    static int coord_center_cluster = 0;

    public static void main(String[] args) throws IOException {

        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

        WritableRaster r = getRaster_input();
        int count_iterations_in_cluster = 0;
        int count_break_while = 0;
        int all_count_iterations_in_cluster = 0;








        System.out.print(Arrays.asList(ResultArray).indexOf(-123));
        System.out.print(Arrays.stream(ResultArray).min());


        while (all_count_iterations_in_cluster < ((w - 4) * (h - 5)))
        {

            rand_value = new Random().nextInt((w - 4) * (h - 5));
            rand_value = rand_value<(h * 4)?(h * 4):rand_value;
            count_iteration_in_cl = 1;


            value_center_cluster = ResultArray[rand_value];
            ResultArray[rand_value] = ResultArray[rand_value]*(-1) - rand_value;
            coord_center_cluster = ResultArray[rand_value];


            count_center_cluster++;
            index_points_cluster.add(value_center_cluster);

            while (Arrays.stream(ResultArray).min().getAsInt()<0) {


                IntStream.rangeClosed(0,8).forEach(RegionGrowingFour::reseach_RG_for_stream);

                ResultArray[rand_value] = Math.abs(ResultArray[rand_value]) - rand_value;

                rand_value = (Arrays.stream(ResultArray).min().getAsInt() + value_center_cluster) * (-1);
                coord_center_cluster = ResultArray[rand_value];
                count_iterations_in_cluster++;
                count_center_cluster--;
            }



            all_count_iterations_in_cluster += count_center_cluster;
            count_iteration_in_cl = 0 ;
            count_iterations_in_cluster =0;



            System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                    +  System.getProperty("line.separator")+
                    "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis()))
                    +  System.getProperty("line.separator")+
                    "Количествоа итераций:" + all_count_iterations_in_cluster +  System.getProperty("line.separator"));

            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
            assert image != null;
            image.setData(r);
            ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

            System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                    +  System.getProperty("line.separator")+
                    "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));

        }

        r.setPixels(1, 1, w - 1, h - 1, ResultArray);
        assert image != null;
        image.setData(r);
        ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

        System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                +  System.getProperty("line.separator")+
                "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));

    }


    public static void reseach_RG_for_stream(int i) {

            int coord_center_c = (coord_center_cluster + value_center_cluster) * (-1);


                i = i==4?i+1:i;
                int coordinate_offset = i < 3 ? (i - 1 - w) : (i > 2 & i < 6) ? (i - 4) : (i - 7 + w);
                int res = Math.abs(Math.abs((ResultArray[coord_center_c])) - Math.abs(ResultArray[coord_center_c + coordinate_offset]) - coord_center_c);

                if (res < limit_for
                        & ResultArray[coord_center_c + coordinate_offset] >= 0
                        & index_points_cluster.indexOf(ResultArray[coord_center_c + coordinate_offset]) == -1
                        & (coord_center_c) > (w * 3) & (coord_center_c) < (w - 4) * (h - 5)) {

                    count_center_cluster++;

                    ///1. координаты центра по смещение = массив с координтами центра - координаты центра - смещение относительно центра
                    ResultArray[coord_center_c + coordinate_offset] = (ResultArray[coord_center_c] + coordinate_offset);


                    test_index_points_cluster.add(coord_center_c + coordinate_offset);
                } else {
                    count_center_cluster--;
                    ResultArray[coord_center_c + coordinate_offset] = ResultArray[coord_center_c];
                }

        count_iteration_in_cl++;


    }



    public static WritableRaster getRaster_input() {
        limit_for = 100;
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


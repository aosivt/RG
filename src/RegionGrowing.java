import breeze.optimize.linear.LinearProgram;
import jodd.typeconverter.Convert;
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
import java.util.stream.IntStream;

/**
 * Created by alex on 20.06.16.
 */
public class RegionGrowing {
    static Integer current_point;
    static ArrayList<Integer> index_points_analyzed = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_in_region = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_other_cl = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_cluster = new ArrayList<Integer>();
    static int[] ResultArray;
    static Integer limit_for;
    static BufferedImage image = null;


    static int h;
    static int w;

    public static void main(String[] args) throws IOException {

        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

        WritableRaster r = getRaster_input();
        int count_iterations_in_cluster = 0;
        int all_count_iterations_in_cluster = 0;
        int[] tempArray = new int[9];
        int tempArray1 = 0;

        while (all_count_iterations_in_cluster < ((w - 4) * (h - 5)) & index_points_cluster.size()<Math.ceil(255/limit_for)) {

            int rand_value = new Random().nextInt((w - 4) * (h - 5));
            rand_value = rand_value<(h * 4)?(h * 4):rand_value;
//            int h_ind = (int) Math.ceil((Double.valueOf(rand_value)/Double.valueOf(w)));
//            int w_ind = w - (h_ind*w - rand_value);
            index_points_analyzed.add(rand_value);

            while (!index_points_analyzed.isEmpty()) {

                tempArray[0] = ResultArray[rand_value - w - 1];
                tempArray[1] = ResultArray[rand_value - w];
                tempArray[2] = ResultArray[rand_value - w + 1];
                tempArray[3] = ResultArray[rand_value - 1];
                tempArray[4] = ResultArray[rand_value];
                tempArray[5] = ResultArray[rand_value + 1];
                tempArray[6] = ResultArray[rand_value + w - 1];
                tempArray[7] = ResultArray[rand_value + w];
                tempArray[8] = ResultArray[rand_value + w + 1];

                if (index_points_cluster.isEmpty() | index_points_cluster.indexOf(tempArray[4]) == -1 ) {
                    index_points_cluster.add(tempArray[4]);
                }

                tempArray = reseach_RG_array(tempArray, rand_value);

//                    index_points_analyzed.remove(0);


                tempArray1 = tempArray[4];

                ResultArray[rand_value - w - 1] = tempArray[0];
                ResultArray[rand_value - w] = tempArray[1];
                ResultArray[rand_value - w + 1] = tempArray[2];
                ResultArray[rand_value - 1] = tempArray[3];
                ResultArray[rand_value] = tempArray[4];
                ResultArray[rand_value + 1] = tempArray[5];
                ResultArray[rand_value + w - 1] = tempArray[6];
                ResultArray[rand_value + w] = tempArray[7];
                ResultArray[rand_value + w + 1] = tempArray[8];



//                index_points_analyzed.add(rand_value);

                count_iterations_in_cluster++;


                if (!index_points_analyzed.isEmpty()) {
                    if (index_points_analyzed.indexOf(rand_value)!=-1)
                    {
                        index_points_analyzed.remove(index_points_analyzed.indexOf(rand_value));
                    }
                    if (!index_points_analyzed.isEmpty()) {
                        rand_value = index_points_analyzed.get(0);
                    }

//                    rand_value = rand_value < (w * 3) ? (w * 3) : rand_value ;
                }
                if (count_iterations_in_cluster>((w - 4) * (h - 5)))
                {
                    System.out.println(ArrayUtils.toString(tempArray) + "||" + tempArray1);

                }

            }

            all_count_iterations_in_cluster += count_iterations_in_cluster;
            count_iterations_in_cluster =0;
            index_points_other_cl.clear();

//            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
//            assert image != null;
//            image.setData(r);
//            ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

        }

//                r.getPixels(w_ind - 1, h_ind - 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind - 1, h_ind - 1, 3, 3, tempArray);
//
//                r.getPixels(w_ind, h_ind - 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind, h_ind - 1, 3, 3, tempArray);
//
//                r.getPixels(w_ind + 1, h_ind - 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind + 1, h_ind - 1, 3, 3, tempArray);
//
//                r.getPixels(w_ind - 1, h_ind , 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind - 1, h_ind , 3, 3, tempArray);
//
//                r.getPixels(w_ind + 1, h_ind , 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind + 1, h_ind , 3, 3, tempArray);
//
//                r.getPixels(w_ind - 1, h_ind + 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind - 1, h_ind + 1, 3, 3, tempArray);
//
//                r.getPixels(w_ind, h_ind + 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind , h_ind + 1, 3, 3, tempArray);
//
//                r.getPixels(w_ind + 1, h_ind + 1, 3, 3, tempArray);
//                tempArray = reseach_RG_array(tempArray, (((h_ind - 1) * h) + w_ind));
//                r.setPixels(w_ind + 1, h_ind + 1, 3, 3, tempArray);

        r.setPixels(1, 1, w - 1, h - 1, ResultArray);
        assert image != null;
        image.setData(r);
        ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

        System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                         +  System.getProperty("line.separator")+
                         "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));

    }

    //    метод для расчета сегмента
    public static int[] reseach_RG_array(int[] input_array, Integer coord_center) {
//        if (!index_points_analyzed.contains(coord_center)) {

        for (int i = 0; i < input_array.length; i++) {
            i = i == 4 ? 5:i;
            if (!Objects.equals(input_array[i], reseach_RG(input_array[4], input_array[i])) & index_points_cluster.indexOf(input_array[i]) == -1) {

                input_array[i] = reseach_RG(input_array[4], input_array[i]);

                if (i < 3 & (coord_center) > (w * 3) & (coord_center) < (w - 4) * (h - 5)) {
                    index_points_analyzed.add(coord_center + i - 1 - w);
                } else if (i > 2 & i < 6  & (coord_center) > (w * 3) & (coord_center) < (w - 4) * (h - 5)) {
                    index_points_analyzed.add(coord_center + i - 4 );
                } else if (i > 5 & (coord_center) > (w * 3) & (coord_center) < (w - 4) * (h - 5)) {
                    index_points_analyzed.add(coord_center + i - 7 + w);
                }

            } else {
                i = i == 4 ? 5:i;
                if (i < 3 & index_points_analyzed.indexOf(coord_center + i - 1 - w) != -1) {
                    index_points_analyzed.remove(index_points_analyzed.indexOf(coord_center + i - 1 - w));
//                    index_points_other_cl.add(coord_center + i - 1 - w);
                } else if (i > 2 & i < 6 & index_points_analyzed.indexOf(coord_center + i - 4) != -1) {
                    index_points_analyzed.remove(index_points_analyzed.indexOf(coord_center + i - 4));
//                    index_points_other_cl.add(coord_center + i - 4);
                } else if (i > 5 & index_points_analyzed.indexOf(coord_center + i - 7 + w) != -1) {
                    index_points_analyzed.remove(index_points_analyzed.indexOf(coord_center + i - 7 + w));
//                    index_points_other_cl.add(coord_center + i - 7 + w);
                }

            }

        }

        return input_array;
//        }


//        return null;
    }

    public static Integer reseach_RG(Integer center_cl, Integer compare_point_value) {
        Integer res = Math.abs(center_cl - compare_point_value);
        if (res < limit_for) {
            return center_cl;
//            return 255;

        } else {
            return compare_point_value;
        }

    }

    public static Point getCoordPoint(int _rand_value) {
        int h_ind = (int) Math.ceil((_rand_value / w));
        int w_ind = w - (_rand_value - h_ind * w);
        return (new Point(h_ind, w_ind));
    }

    public static WritableRaster getRaster_input() {
        limit_for = 100;
        Integer random_start_point = null;
        Random random;

        byte band_number = 0;
        File TiffPath = new File("input");
        File OutPutPath = new File("outputfile");
        File[] outlistFile = OutPutPath.listFiles();
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


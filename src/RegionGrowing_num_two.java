/**
 * Created by oshchepkovayu on 11.08.16.
 */

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


public class RegionGrowing_num_two {
    static Integer current_point;
    static ArrayList<Integer> index_points_analyzed = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_in_region = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_other_cl = new ArrayList<Integer>();
    static ArrayList<Integer> index_points_cluster = new ArrayList<Integer>();
    static int[] ResultArray;
    static int[] ResultArray_temp;
    static Integer limit_for;
    static Integer count_iteration_in_cl;
    static BufferedImage image = null;


    static int h;
    static int w;

    public static void main(String[] args) throws IOException {

        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

        WritableRaster r = getRaster_input();
        int count_iterations_in_cluster = 0;
        int count_break_while = 0;
        int all_count_iterations_in_cluster = 0;
        int[] tempArray = new int[9];
        int tempArray1 = 0;

        ResultArray_temp = ArrayUtils.clone(ResultArray);
        while (all_count_iterations_in_cluster < ((w - 4) * (h - 5))) {


            int rand_value = new Random().nextInt((w - 4) * (h - 5));
            rand_value = rand_value<(h * 4)?(h * 4):rand_value;
            count_iteration_in_cl = 1;
            if (ResultArray_temp[rand_value]>=0) {
                index_points_analyzed.add(rand_value);
                index_points_cluster.add(ResultArray[rand_value]);
            } else if (count_break_while > w)
            {break;}
            else
            {count_break_while++;}



            while (!index_points_analyzed.isEmpty()) {


ResultArray_temp[rand_value - w - 1]    = reseach_RG(ResultArray[rand_value],ResultArray[rand_value - w - 1],           rand_value,rand_value - w - 1);
ResultArray[rand_value - w - 1]         = Math.abs(ResultArray_temp[rand_value - w - 1]);
ResultArray_temp[rand_value - w]        = reseach_RG(ResultArray[rand_value],ResultArray[rand_value - w],               rand_value,rand_value - w);
ResultArray[rand_value - w]             = Math.abs(ResultArray_temp[rand_value - w]);
ResultArray_temp[rand_value - w + 1]    = reseach_RG(ResultArray[rand_value],ResultArray[rand_value - w + 1],           rand_value,rand_value - w + 1);
ResultArray[rand_value - w + 1]         = Math.abs(ResultArray_temp[rand_value - w + 1]);
ResultArray_temp[rand_value - 1]        = reseach_RG(ResultArray[rand_value],ResultArray[rand_value - 1],               rand_value,rand_value - 1);
ResultArray[rand_value - 1]             = Math.abs(ResultArray_temp[rand_value - 1]);
ResultArray_temp[rand_value + 1]        = reseach_RG(ResultArray[rand_value],ResultArray[rand_value + 1],               rand_value,rand_value + 1);
ResultArray[rand_value + 1]             = Math.abs(ResultArray_temp[rand_value + 1]);
ResultArray_temp[rand_value + w - 1]    = reseach_RG(ResultArray[rand_value],ResultArray[rand_value + w - 1],           rand_value,rand_value + w - 1);
ResultArray[rand_value + w - 1]         = Math.abs(ResultArray_temp[rand_value + w - 1]);
ResultArray_temp[rand_value + w]        = reseach_RG(ResultArray[rand_value],ResultArray[rand_value + w],               rand_value,rand_value + w);
ResultArray[rand_value + w]             = Math.abs(ResultArray_temp[rand_value + w]);
ResultArray_temp[rand_value + w + 1]    = reseach_RG(ResultArray[rand_value],ResultArray[rand_value + w + 1],           rand_value,rand_value + w + 1);
ResultArray[rand_value + w + 1]         = Math.abs(ResultArray_temp[rand_value + w + 1]);


                if (!index_points_analyzed.isEmpty()) {

                    if (!index_points_analyzed.isEmpty()) {
                        rand_value = index_points_analyzed.get(0);
                        index_points_analyzed.remove(0);
                    }


                }
                count_iterations_in_cluster++;
                if (count_iterations_in_cluster>((w - 4) * (h - 5)))
                {
                    System.out.println(ArrayUtils.toString(tempArray) + "||" + tempArray1);

                }

            }

            System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                    +  System.getProperty("line.separator")+
                    "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis()))
                    +  System.getProperty("line.separator")+
                    "Количествоа итераций:" + count_iteration_in_cl+  System.getProperty("line.separator"));

            count_iteration_in_cl = 0 ;
            all_count_iterations_in_cluster += count_iterations_in_cluster;
            count_iterations_in_cluster =0;
            index_points_other_cl.clear();

//            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
//            assert image != null;
//            image.setData(r);
//            ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));
        }

        r.setPixels(1, 1, w - 1, h - 1, ResultArray);
        assert image != null;
        image.setData(r);
        ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

        System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                +  System.getProperty("line.separator")+
                "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));

    }

    public static Integer reseach_RG(Integer center_cl, Integer compare_point_value, Integer coordinate_center,Integer coordinate_compare) {
        Integer res = Math.abs(center_cl - compare_point_value);

        if (index_points_analyzed.indexOf(coordinate_center) != -1) {
            index_points_analyzed.remove(index_points_analyzed.indexOf(coordinate_center));
        }
        if (res < limit_for & ResultArray_temp[coordinate_compare]>=0  & index_points_cluster.indexOf(compare_point_value) == -1
                & (coordinate_center) > (w * 3) & (coordinate_center) < (w - 4) * (h - 5)) {
            count_iteration_in_cl++;
            index_points_analyzed.add(coordinate_compare);
            return center_cl;

        } else {
            if (index_points_analyzed.indexOf(coordinate_compare) != -1 & ResultArray_temp[coordinate_compare]<0) {
                index_points_analyzed.remove(index_points_analyzed.indexOf(coordinate_compare));
                count_iteration_in_cl--;
            }
            return compare_point_value*(-1);
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
        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
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


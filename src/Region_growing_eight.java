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



public class Region_growing_eight {

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

        r = Region_growing_eight.getRaster_input();
        int count_iterations_in_cluster = 0;
        int count_break_while = 0;


        rand_value = get_random_index();
        count_iteration_in_cl = 1;
        value_center_cluster = ResultArray[rand_value];
        count_center_cluster++;
        index_points_cluster.add(value_center_cluster);

//try {


            IntStream.rangeClosed(1,((w - 4) * (h - 5))).parallel().forEach(
                    (b) ->
                    {

                        if (count_iteration_in_cl >= ((w - 4) * (h - 5) * 2) & index_points_cluster.size() <= limit_for) {
                            rand_value = get_random_index();
                            all_count_iteration_in_cl ++;
                            count_iteration_in_cl = 1;
                            value_center_cluster = ResultArray[rand_value];
                            count_center_cluster++;
                            index_points_cluster.add(value_center_cluster);
                            b = 0;
                        } else if (count_iteration_in_cl >= (w))
                        {

                        return;
                        }
                int index_while = 1;
                while (index_while<=w*2)
                {
                    reseach_RG_for_stream(index_while);
                    index_while ++;
                }


//
//                System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
//                        +  System.getProperty("line.separator")+
//                        "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis()))
//                        +  System.getProperty("line.separator")+
//                        "Количествоа итераций:" + count_iteration_in_cl +  System.getProperty("line.separator"));

            }
            );






        r.setPixels(1, 1, w - 1, h - 1, ResultArray);
        assert image != null;
        image.setData(r);
        ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));

        System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                +  System.getProperty("line.separator")+
                "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis())));

    }


    public static void reseach_RG_for_stream(int i) {
        //общее смещение от центра
        int offset = 2*i;
        //ширина выделенной области изображения
        int width_region = offset+1;

        //максимальная величина смещения
        int max_offset = (w - 1) * i + i;

        //периметр региона
        int perimetr_region = 4 * width_region - 4;

        int index_while = 0;
        int coord_center_c = rand_value;



        int coordinate_offset = 0;

        int index_start = 0;

        while (index_while<perimetr_region/2)
        {

            if(index_while<width_region)
            {
                coordinate_offset = max_offset - index_while;
                index_start += reseach_result_array(coord_center_c,max_offset - index_while,width_region-1,0)?index_start:0;
            }
            else
            {
                //прибавили 1 к index_while для смещение на одну строку ниже
                index_start += reseach_result_array(coord_center_c,coordinate_offset - ((index_while-(width_region-1))*(w-1)),0,width_region-1)?index_start:0;
            }
            index_while++;
        }
        if (index_start==0)
        {
//            Thread.interrupted();
            Thread.currentThread().interrupt();



        }


//        try {
//            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
//            assert image != null;
//            image.setData(r);
//            ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public static boolean reseach_result_array(int coord_center_c,int coordinate_offset,int width_region,int row_offset)
    {
        int res_top_row = 0;
        int res_bottom_row = 0;
        int offset_one = coord_center_c - coordinate_offset;
        int offset_two = row_offset == 0 ? offset_one + width_region * w - width_region: offset_one - row_offset;
        boolean result_return = false;

        if ((offset_one) > (w) & (offset_one) < (w-2) * (h-2))
        {
            if (index_points_cluster.indexOf(ResultArray[offset_one]) == -1
//
//                    &
//                    (
//                            ResultArray[offset_one-1]==value_center_cluster |
//                                    ResultArray[offset_one+1]==value_center_cluster |
//
//                                    ResultArray[offset_one+w-1]==value_center_cluster |
//                                    ResultArray[offset_one+w+1]==value_center_cluster |
//
//                                    ResultArray[offset_one-w-1]==value_center_cluster |
//                                    ResultArray[offset_one-w+1]==value_center_cluster |
//
//
//                                    ResultArray[offset_one+w]==value_center_cluster |
//                                    ResultArray[offset_one-w]==value_center_cluster
//
//
//
//                    )
                    ) {
                res_top_row = Math.abs(value_center_cluster - ResultArray[offset_one]);
                ResultArray[offset_one] = res_top_row < limit_for ?
                        value_center_cluster :
                        ResultArray[offset_one];
//                if (
//                        (ResultArray[offset_one-1] !=   value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one-1])==-1)         |
//                        (ResultArray[offset_one+1] !=   value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one+1])==-1)         |
//
//                        (ResultArray[offset_one+w-1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one+w-1])==-1)       |
//                        (ResultArray[offset_one+w+1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one+w+1])==-1)       |
//
//                        (ResultArray[offset_one-w-1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one-w-1])==-1)       |
//                        (ResultArray[offset_one-w+1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one-w+1])==-1)       |
//
//                        (ResultArray[offset_one+w]!=    value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one+w])==-1)         |
//                        (ResultArray[offset_one-w]!=    value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_one-w])==-1 )        )
//                {
//                    res_top_row = Math.abs(value_center_cluster - ResultArray[offset_one]);
//                    ResultArray[offset_one] = res_top_row < limit_for ?
//                            value_center_cluster :-1;
//                }



                count_iteration_in_cl++;
                result_return = true;
            }
        }
        if ((offset_two) < (w-2) * (h-2) & (offset_two) > w)
        {
            if (index_points_cluster.indexOf(ResultArray[offset_two]) == -1
//                    &
//                    (
//                                    ResultArray[offset_two-1]==value_center_cluster |
//                                    ResultArray[offset_two+1]==value_center_cluster |
//
//                                    ResultArray[offset_two+w-1]==value_center_cluster |
//                                    ResultArray[offset_two+w+1]==value_center_cluster |
//
//                                    ResultArray[offset_two-w-1]==value_center_cluster |
//                                    ResultArray[offset_two-w+1]==value_center_cluster |
//
//                                    ResultArray[offset_two+w]==value_center_cluster |
//                                    ResultArray[offset_two-w]==value_center_cluster
//                    )

                    ) {
                res_bottom_row = Math.abs(value_center_cluster - ResultArray[offset_two]);
                ResultArray[offset_two] = res_bottom_row < limit_for ?
                        value_center_cluster :
                        ResultArray[offset_two];
//                if (
//                        (ResultArray[offset_two-1] !=   value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two-1])==-1)         |
//                        (ResultArray[offset_two+1] !=   value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two+1])==-1)         |
//
//                        (ResultArray[offset_two+w-1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two+w-1])==-1)       |
//                        (ResultArray[offset_two+w+1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two+w+1])==-1)       |
//
//                        (ResultArray[offset_two-w-1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two-w-1])==-1)       |
//                        (ResultArray[offset_two-w+1]!=  value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two-w+1])==-1)       |
//
//                        (ResultArray[offset_two+w]!=    value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two+w])==-1)         |
//                        (ResultArray[offset_two-w]!=    value_center_cluster & index_points_cluster.indexOf(ResultArray[offset_two-w])==-1 )        )
//
//                        {
//                            res_bottom_row = Math.abs(value_center_cluster - ResultArray[offset_two]);
//                            ResultArray[offset_two] = res_bottom_row < limit_for ?
//                                    value_center_cluster :
//                                    -1;
//                        }



                count_iteration_in_cl++;
                result_return = true;
            }

        }
//        try {
//            r.setPixels(1, 1, w - 1, h - 1, ResultArray);
//            assert image != null;
//            image.setData(r);
//            ImageIO.write(image, "tiff", new File("output/result_"+limit_for+".tiff"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return result_return;
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
        limit_for = 10;
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


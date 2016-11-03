/**
 * Created by oshchepkovayu on 15.08.16.
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

public class RegionGrowing_five {

    static List<Integer> index_points_cluster = new ArrayList<Integer>();
    static List<Integer> test_index_points_cluster = new ArrayList<Integer>();
    static List<Integer> without_index_points_cluster = new ArrayList<Integer>();

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



        while (all_count_iterations_in_cluster < ((w - 4) * (h - 5)))
        {
            rand_value = get_random_index();


            count_iteration_in_cl = 1;


            value_center_cluster = ResultArray[rand_value];

            count_center_cluster++;
            index_points_cluster.add(value_center_cluster);


            IntStream.rangeClosed(1,w).forEach(RegionGrowing_five::reseach_RG_for_stream);

//            ResultArray[rand_value] = Math.abs(ResultArray[rand_value]) - rand_value;


            all_count_iterations_in_cluster += count_iteration_in_cl;
            count_iteration_in_cl = 0 ;




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

            all_count_iterations_in_cluster = all_count_iterations_in_cluster + ((w - 4) * (h - 5));

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
        //общее смещение от центра
        int offset = 2*i;
        //ширина выделенной области изображения
        int width_region = offset+1;

        //максимальная величина смещения
        int max_offset = w * i + i;

        int perimetr_region = 4 * width_region - 4;

        int index_while = 0;
        int coord_center_c = rand_value;

        int row_images = (int) Math.ceil((double)rand_value/(double)w);
        int col_images = rand_value - row_images*w + w;

        int coord_comp_point = 0;
        int row_region = 0;
        int col_region = 0;

        int coordinate_offset = 0;
        int res_top_row = 0;
        int res_bottom_row = 0;
        int index_for_offset = 1;
        while (index_while<=perimetr_region/2)
        {
//            row_region = (int) Math.ceil((double)(index_while)/(double)width_region);
//            col_region = (index_while) - row_region*width_region + width_region;

//            coordinate_offset = max_offset - index_while-1;
            if(index_while<width_region)
            {
//                coordinate_offset = max_offset - row_region * width_region ;
                coordinate_offset = max_offset - index_while;
                reseach_result_array(coord_center_c,coordinate_offset);
//                index_while = col_region==1?index_while + width_region-3:index_while;
            }
            else
            {
//                coord_center_c + coordinate_offset - index_for_offset*w
//                coord_center_c - coordinate_offset + index_for_offset*w;
                reseach_result_array(coord_center_c,coordinate_offset + index_for_offset*w);
                index_for_offset ++;
            }


//            if (row_region >= 2 & row_region <= width_region - 1)
//            {
//                coordinate_offset = max_offset - row_region * width_region ;
//                index_while = col_region==1?index_while + width_region-3:index_while;
//            }
//
//            if ((coord_center_c - coordinate_offset) > (w))
//            {
//                if (index_points_cluster.indexOf(ResultArray[coord_center_c - coordinate_offset]) == -1) {
//                    res_top_row = Math.abs(value_center_cluster - ResultArray[coord_center_c - coordinate_offset]);
//                    ResultArray[coord_center_c - coordinate_offset] = res_top_row < limit_for ?
//                            value_center_cluster :
//                            ResultArray[coord_center_c - coordinate_offset];
//                    count_center_cluster ++;
//                }
//            }
//            if ((coord_center_c + coordinate_offset) < (w-2) * (h-2))
//            {
//                if (index_points_cluster.indexOf(ResultArray[coord_center_c + coordinate_offset]) == -1) {
//                    res_bottom_row = Math.abs(value_center_cluster - ResultArray[coord_center_c + coordinate_offset]);
//                    ResultArray[coord_center_c + coordinate_offset] = res_bottom_row < limit_for ?
//                            value_center_cluster :
//                            ResultArray[coord_center_c + coordinate_offset];
//                    count_center_cluster ++;
//                }
//            }
//            coordinate_offset = 0;
        index_while++;
        }

        }

    public static void reseach_result_array(int coord_center_c,int coordinate_offset)
    {
        int res_top_row = 0;
        int res_bottom_row = 0;

            if ((coord_center_c - coordinate_offset) > (w))
            {
                if (index_points_cluster.indexOf(ResultArray[coord_center_c - coordinate_offset]) == -1) {
                    res_top_row = Math.abs(value_center_cluster - ResultArray[coord_center_c - coordinate_offset]);
                    ResultArray[coord_center_c - coordinate_offset] = res_top_row < limit_for ?
                            value_center_cluster :
                            ResultArray[coord_center_c - coordinate_offset];
                    count_center_cluster ++;
                }
            }
            if ((coord_center_c + coordinate_offset) < (w-2) * (h-2))
            {
                if (index_points_cluster.indexOf(ResultArray[coord_center_c + coordinate_offset]) == -1) {
                    res_bottom_row = Math.abs(value_center_cluster - ResultArray[coord_center_c + coordinate_offset]);
                    ResultArray[coord_center_c + coordinate_offset] = res_bottom_row < limit_for ?
                            value_center_cluster :
                            ResultArray[coord_center_c + coordinate_offset];
                    count_center_cluster ++;
                }
            }
            coordinate_offset = 0;


    }
public static int get_random_index()
{
    int index_while = 0;
    int rand = -1;
    while (index_while<w)
    {
        rand = new Random().nextInt((w - 4) * (h));
        rand = rand <(h * 3)?(h * 3):rand;
        if (index_points_cluster.indexOf(ResultArray[rand])==-1)
        {
            index_while = w;
        }
//        else{index_while++;}
    }

    return rand;
}

    public static WritableRaster getRaster_input() {
        limit_for = 150;
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


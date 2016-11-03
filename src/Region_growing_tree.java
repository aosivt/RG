/**
 * Created by oshchepkovayu on 11.08.16.
 */


import org.apache.commons.lang3.ArrayUtils;

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


public class Region_growing_tree {


    static List<Integer> index_points_analyzed = new ArrayList<>();
    static List<Integer> index_points_analyzing = new ArrayList<>();


    static List<Integer> index_points_cluster = new ArrayList<Integer>();
    static int[] ResultArray;
    static int[] ResultArray_temp;
    static Integer limit_for;
    static Integer count_iteration_in_cl;
    static BufferedImage image = null;
    static int rand_value = 0;


    static int h;
    static int w;

    public static void main(String[] args) throws IOException {

        Date StartPro = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");

        WritableRaster r = getRaster_input();
        int count_iterations_in_cluster = 0;
        int count_break_while = 0;
        int all_count_iterations_in_cluster = 0;


        ResultArray_temp = ArrayUtils.clone(ResultArray);
        while (all_count_iterations_in_cluster < ((w - 4) * (h - 5))) {


            rand_value = new Random().nextInt((w - 4) * (h - 5));
            rand_value = rand_value<(h * 4)?(h * 4):rand_value;
            count_iteration_in_cl = 1;
            if (ResultArray_temp[rand_value]>=0) {
                index_points_analyzing.add(rand_value);
                index_points_cluster.add(ResultArray[rand_value]);
            } else if (count_break_while > 10)
            {break;}
            else
            {count_break_while++;}



            while (!index_points_analyzing.isEmpty()) {


                IntStream.rangeClosed(0,8).parallel().forEach(Region_growing_tree::reseach_RG_for_stream);


                    if (index_points_analyzing.iterator().hasNext()) {
                        try {

                            rand_value = index_points_analyzing.iterator().next().intValue();
                            index_points_analyzed.add(rand_value);
//                            Set<Integer> hs = new HashSet<>();
//                            hs.addAll(index_points_analyzed);
//
//                            index_points_analyzed.clear();
//                            index_points_analyzed.addAll(hs);



                            index_points_analyzing.removeAll(index_points_analyzed);
                            index_points_analyzed.clear();
                        }catch (Exception e)
                        {
//                            rand_value = index_points_analyzing.iterator().next().intValue();
//                            index_points_analyzed.add(rand_value);
//                            continue;
                            break;
                        }


                    }


                count_iterations_in_cluster++;
//                if (count_iterations_in_cluster>((w - 4) * (h - 5)))
//                {
//                    System.out.println(ArrayUtils.toString(tempArray) + "||" + tempArray1);
//
//                }

            }

            index_points_analyzing.clear();
            index_points_analyzed.clear();
            count_iteration_in_cl = 0 ;
            all_count_iterations_in_cluster += count_iterations_in_cluster;
            count_iterations_in_cluster =0;



            System.out.print("Время старта обработки:"    + timeFormat.format(StartPro)
                    +  System.getProperty("line.separator")+
                    "Время окончания обработки:" + timeFormat.format(new Date(System.currentTimeMillis()))
                    +  System.getProperty("line.separator")+
                    "Количествоа итераций:" + all_count_iterations_in_cluster +  System.getProperty("line.separator"));
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


    public static void reseach_RG_for_stream(int i) {

        int coordinate_offset =  i < 3 ? (i - 1 - w) : (i > 2 & i < 6) ? (i - 4): (i - 7 + w) ;

        Integer res = Math.abs(ResultArray[rand_value] - ResultArray[rand_value+coordinate_offset]);

        if (res < limit_for
                & ResultArray_temp[rand_value+coordinate_offset]>=0
                & index_points_cluster.indexOf(ResultArray[rand_value+coordinate_offset]) == -1
                & (rand_value) > (w * 3) & (rand_value) < (w - 4) * (h - 5)) {
            count_iteration_in_cl++;
            index_points_analyzing.add(rand_value+coordinate_offset);
            ResultArray[rand_value+coordinate_offset] = ResultArray[rand_value];

        } else {
            if (      index_points_analyzing.indexOf(rand_value+coordinate_offset) != -1
                    & ResultArray_temp[rand_value+coordinate_offset]<0)
            {
                index_points_analyzed.add(rand_value+coordinate_offset);
                count_iteration_in_cl--;
            }
            ResultArray_temp[rand_value+coordinate_offset] = ResultArray[rand_value+coordinate_offset] * (-1);

        }

    }



    public static WritableRaster getRaster_input() {
        limit_for = 50;
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

//                            index_points_analyzing.set(0,index_points_analyzing.get(1));
//                            rand_value = index_points_analyzing.get(0);
//                            index_points_analyzing.set(index_points_analyzing.indexOf(rand_value),-1);
//                            Collections.sort(index_points_analyzing, Collections.reverseOrder());
//                            Set<Integer> hs = new HashSet<>();
//                            hs.addAll(index_points_analyzing);
//
//                            index_points_analyzing.clear();
//                            index_points_analyzing.addAll(hs);



//                            index_points_analyzed.remove(index_points_analyzed.indexOf((int)index_points_analyzed.toArray()[0]));


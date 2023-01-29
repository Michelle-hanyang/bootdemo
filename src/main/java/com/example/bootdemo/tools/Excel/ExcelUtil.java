package com.example.bootdemo.tools.Excel;
import com.example.bootdemo.tools.Exception.CommonException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static jdk.internal.org.objectweb.asm.Type.BOOLEAN;
import static netscape.security.Privilege.BLANK;

/**
 * Author: YANG
 * Date: 2023/1/29 9:11
 * Describe:
 */
public class ExcelUtil {
    public File getFile(String url){
        File file=new File(url);
        return file;
    }
    public Workbook workbook;
    /**
     * 根据url获取workbook
     * @param url
     * @return
     * @throws Exception
     */
    public  Workbook getWorkbook(String url) throws Exception {
        FileInputStream f=new FileInputStream(url);
        return getWorkbook(f);
    }

    /**
     * 根据file获取workbook
     * @param file
     * @return
     * @throws Exception
     */
    public  Workbook getWorkbook(File file) throws CommonException {
        try {
            workbook= WorkbookFactory.create(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommonException("文件格式不符");
        }finally{
            file.delete();
        }
        return workbook;
    }
    public Workbook getWorkbook(FileInputStream fos) throws Exception {
        try {
            workbook=WorkbookFactory.create(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }finally{
            if(fos!=null){
                fos.close();
            }
        }
        return workbook;
    }

    /**
     * 获取单元格值
     * @param cell
     * @return
     */
    public String getCellValue(Cell cell){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String value="";
        if(cell!=null){
            switch (cell.getCellType()){
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value=String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    switch (cell.getCachedFormulaResultType()){
                        case Cell.CELL_TYPE_NUMERIC:
                            if(DateUtil.isCellDateFormatted(cell)){
                                Date date=cell.getDateCellValue();
                                value=sdf.format(date);
                            }else{
                                BigDecimal n=new BigDecimal(cell.getNumericCellValue());
                                DecimalFormat decimalFormat=new DecimalFormat("0");
                                decimalFormat.setMaximumFractionDigits(18);
                                value=decimalFormat.format(n.doubleValue());
                            }
                            break;
                        case Cell.CELL_TYPE_STRING:
                            value=String.valueOf(cell.getStringCellValue());
                            if(value!=null){
                                value=value.trim();
                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            value=String.valueOf(cell.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            break;
                        default:
                            value=cell.getRichStringCellValue().getString();
                            break;
                    }
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if(DateUtil.isCellDateFormatted(cell)){
                        Date date=cell.getDateCellValue();
                        value=sdf.format(date);
                    }else{
                        BigDecimal n=new BigDecimal(cell.getNumericCellValue());
                        DecimalFormat decimalFormat=new DecimalFormat("0");
                        decimalFormat.setMaximumFractionDigits(18);
                        value=decimalFormat.format(n.doubleValue());
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value=String.valueOf(cell.getStringCellValue());
                    if(value!=null){
                        value=value.trim();
                    }
                    break;
                default:
                    value=cell.getRichStringCellValue().getString();
                    break;
            }
//            int type = cell.getCellType() ;
//            if(type == Cell.CELL_TYPE_STRING){
//                value = cell.getStringCellValue() ;
//            }else if(type==Cell.CELL_TYPE_NUMERIC){
//                if(DateUtil.isCellDateFormatted(cell)){
////                    value= String.valueOf(cell.getDateCellValue());
//                    value=sdf.format(cell.getDateCellValue());
//                } else{
//                    value =  String.valueOf(cell.getNumericCellValue());
//                }
//            }else if(type==Cell.CELL_TYPE_BOOLEAN){
//                value =  String.valueOf(cell.getBooleanCellValue()) ;
//            }else if(type==Cell.CELL_TYPE_FORMULA){
//                try {
//                    value = String.valueOf(cell.getNumericCellValue());
//                } catch (IllegalStateException e) {
//                    value = String.valueOf(cell.getRichStringCellValue());
//                }
//            }
        }
        return value.trim();

    }

    /**
     * 获取sheet列表
     * @return
     */
    public List<Sheet> getSheetList(){
        List<Sheet> sheetList=new ArrayList<>();
        for(int numSheet=0;numSheet<workbook.getNumberOfSheets();numSheet++){
            Sheet sheet=workbook.getSheetAt(numSheet);
            sheetList.add(sheet);
        }
        return sheetList;
    }
    //解析整个表
    public  List<List<Map<String,String>>> getObject() throws CommonException {
        List<List<Map<String,String>>> obj =new ArrayList<>();
        for(int numSheet=0;numSheet<workbook.getNumberOfSheets();numSheet++){
            Sheet sheet=workbook.getSheetAt(numSheet);
            if(sheet==null){
                continue;
            }
            List<Map<String,String>> listRow=new ArrayList<Map<String,String>>();
            for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
                Map<String,String> mapCell=new HashMap<String, String>();
                Row row=sheet.getRow(rowNum);
                if(row==null||row.getLastCellNum()<=0) continue;
                for(Cell cell:row){
                    mapCell.put(cell.getAddress().toString(),getCellValue(cell));
                }
                listRow.add(mapCell);
            }
            obj.add(listRow);
        }
        return obj;
    }
    /**
     *
     * @param formula 计算公式
     * @param ms
     * @param num 去除map中key的行信息
     * @return
     * @throws ScriptException
     */
//    public  String getFormulaValueByEl(String formula, Map<String,String> ms, int num) throws CommonException {
//        Context context= Lang.context();
//        String s=formula.replaceAll("\\(","").replaceAll("\\)","").replaceAll("\\+",",").replaceAll("\\-",",").replaceAll("\\*",",").replaceAll("\\/",",");
//        List<String> s1= Arrays.asList(s.split(","));
//        for(String se:s1){
//            String val=ms.get(se+num);
//            try {
//                String val1= Strings.isEmpty(val)?"0":val;
//                //用float运算会出现精度丢失问题
//                Float val2=Float.valueOf(val1);
//                context.set(se,val2);
//            }catch (Exception e){
//                throw new CommonException(se+num+"数据出现问题");
//            }
//        }
//        return getFormulaValue(context,formula);
//    }
//    /**
//     *  nutz表达式引擎公式计算
//     * @param context
//     * @param formula
//     * @return
//     * @throws Exception
//     */
//    public static String getFormulaValue( Context context,String formula){
////        float result= (float) El.eval(context,formula);
//        BigDecimal b=new BigDecimal((float) El.eval(context,formula));
//        String result=b.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
//        return result;
//    }
}

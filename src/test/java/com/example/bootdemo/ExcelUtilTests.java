package com.example.bootdemo;

import com.example.bootdemo.tools.Excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Author: YANG
 * Date: 2023/2/8 14:20
 * Describe:
 */
@SpringBootTest
public class ExcelUtilTests {
    @Test
    public void excelToMap() throws Exception {
        ExcelUtil excelUtil=new ExcelUtil();
        Workbook work=excelUtil.getWorkbook("F:\\统计.xls");
        List<List<Map<String,String>>> lists=excelUtil. getObject();
        System.out.println(lists.toString());
    }
}

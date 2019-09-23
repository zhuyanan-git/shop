package com.qfant.admin;

import com.qfant.utils.page.TableDataInfo;
import com.qfant.wx.entity.Member;
import com.qfant.wx.service.MemberService;

import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController extends BaseController{

    @Autowired
    private MemberService memberService;

    @GetMapping()
    public String member(){ return "member/member";}
    //查询会员信息
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Member member){
        startPage();
        List<Member> list = memberService.selectMemberList(member);
        return getDataTable(list);
    }

    //会员数据导出
    @RequestMapping(value = "UserExcelDownloads", method = RequestMethod.GET)
    public void downloadAllClassmate(HttpServletResponse response,Member member) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

        List<Member> memberList = memberService.selectMemberList(member);

        String fileName = "userinf"  + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = { "id", "姓名", "电话", "会员卡编号","性别","生日","openid","昵称","积分","余额","状态","会员卡号","卡代码","类型","创建时间","绑定时间"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //在表中存放查询到的数据放入对应的列
        for (Member member1 : memberList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(member1.getId());
            row1.createCell(1).setCellValue(member1.getName());
            row1.createCell(2).setCellValue(member1.getPhone());
            row1.createCell(3).setCellValue(member1.getIdcard());
            row1.createCell(4).setCellValue(member1.getGender());
            row1.createCell(5).setCellValue(member1.getBirthday());
            row1.createCell(6).setCellValue(member1.getOpenid());
            row1.createCell(7).setCellValue(member1.getNickname());
            row1.createCell(8).setCellValue(member1.getBonus());
            row1.createCell(9).setCellValue(member1.getBalance());
            row1.createCell(10).setCellValue(member1.getStatus());
            row1.createCell(11).setCellValue(member1.getCardno());
            row1.createCell(12).setCellValue(member1.getCardcode());
            String type1 = null;
            if(member1.getType()==1){
                type1 = "新会员";
            }else if(member1.getType()==2){
                type1 = "老会员";
            }else {
                type1="-";
            }
            row1.createCell(13).setCellValue(type1);
            String create = sdf.format(member1.getCreatetime());
            row1.createCell(14).setCellValue(create);
            String bind = null;
            if((member1.getBindtime())!=null){
                bind = sdf.format(member1.getBindtime());
            }
            row1.createCell(15).setCellValue(bind);

            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }



}

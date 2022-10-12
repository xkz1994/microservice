package com.aacoptics.data.analysis.controller;

import com.aacoptics.common.core.vo.Result;
import com.aacoptics.data.analysis.entity.form.QueryParams;
import com.aacoptics.data.analysis.entity.po.AllData;
import com.aacoptics.data.analysis.exception.WlgReportErrorType;
import com.aacoptics.data.analysis.service.IAllDataService;
import com.aacoptics.data.analysis.util.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/allData")
@Api("AllDataController")
@Slf4j
public class AllDataController {

    @Resource
    IAllDataService allDataService;

    /**
     * 根据条件查询条件数据
     *
     * @param queryParams
     * @return
     */
    @ApiOperation(value = "条件搜索关联数据", notes = "条件搜索关联数据")
    @PostMapping("/getDataByConditions")
    public Result getDataByConditions(@RequestBody QueryParams queryParams) {
        Integer page = queryParams.getCurrent();
        Integer size = queryParams.getSize();
        Page<AllData> iPage = new Page<>(page, size);
        IPage<AllData> res = allDataService.getAllDataByConditionsWithPage(iPage,
                queryParams.getCategory(),
                queryParams.getProject(),
                queryParams.getPartName(),
                queryParams.getMaterial());
        if (res.getTotal() == 0) {
            return Result.fail(WlgReportErrorType.BUSINESS_EXCEPTION, "查询数据为空！");
        }
        return Result.success(res);
    }


    @ApiOperation(value = "导出所有数据Excel", notes = "导出所有数据Excel")
    @PostMapping(value = "/exportExcel")
    public void exportAllDataExcel(@RequestBody QueryParams queryParams, HttpServletResponse response) throws Exception {
        XSSFWorkbook wb = null;
        try {
            // 根据查询条件获取数据
            List<AllData> datas = allDataService.getAllDataByConditions(queryParams);
            // 读取模板
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("excelTemplate/allData.xlsx");
            wb = (XSSFWorkbook) WorkbookFactory.create(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);

            if (datas != null && datas.size() > 0) {
                for (int i = 0; i < datas.size(); i++) {
                    AllData p = datas.get(i);
                    // 获取行
                    XSSFRow row = sheet.getRow(i + 3);
                    if (row == null) {
                        row = sheet.createRow(i + 3);
                    }
                    row.createCell(0).setCellValue(p.getCategory());
                    row.createCell(1).setCellValue(p.getProject());
                    row.createCell(2).setCellValue(p.getMoldNo());
                    row.createCell(3).setCellValue(p.getPartName());
                    row.createCell(4).setCellValue(p.getMaterial());
                    row.createCell(5).setCellValue(p.getMoldTemp());
                    row.createCell(6).setCellValue(p.getMaterialTemp());
                    row.createCell(7).setCellValue(p.getJetVelocity());
                    row.createCell(8).setCellValue(p.getVpSwitch());
                    row.createCell(9).setCellValue(p.getHoldPressure1());
                    row.createCell(10).setCellValue(p.getHoldPressure2());
                    row.createCell(11).setCellValue(p.getHoldPressure3());
                    row.createCell(12).setCellValue(p.getHoldPressure4());
                    row.createCell(13).setCellValue(p.getHoldPressure5());
                    row.createCell(14).setCellValue(p.getHoldPressure6());
                    row.createCell(15).setCellValue(p.getHoldTime1());
                    row.createCell(16).setCellValue(p.getHoldTime2());
                    row.createCell(17).setCellValue(p.getHoldTime3());
                    row.createCell(18).setCellValue(p.getHoldTime4());
                    row.createCell(19).setCellValue(p.getHoldTime5());
                    row.createCell(20).setCellValue(p.getHoldTime6());
                    row.createCell(21).setCellValue(p.getHoldPressureVelocity());
                    row.createCell(22).setCellValue(p.getPlatenPosition());
                    row.createCell(23).setCellValue(p.getOpeningSpeed());
                    row.createCell(24).setCellValue(p.getEjectionSpeed());
                    row.createCell(25).setCellValue(p.getCoolingTime());
                    row.createCell(26).setCellValue(p.getClampingForce());
                    row.createCell(27).setCellValue(p.getPassivation());

                    row.createCell(28).setCellValue(p.getCoreThickness());
                    row.createCell(29).setCellValue(p.getCoreThicknessRange());
                    row.createCell(30).setCellValue(p.getR1VectorHeight());
                    row.createCell(31).setCellValue(p.getR1VectorHeightRange());
                    row.createCell(32).setCellValue(p.getR2VectorHeight());
                    row.createCell(33).setCellValue(p.getR2VectorHeightRange());
                    row.createCell(34).setCellValue(p.getOuterDiameterEcc());
                    row.createCell(35).setCellValue(p.getKanheEcc());
                    row.createCell(36).setCellValue(p.getFaceEcc());
                    row.createCell(37).setCellValue(p.getAnnealingProcess());
                    row.createCell(38).setCellValue(p.getKanheRoundness());
                    row.createCell(39).setCellValue(p.getOuterDiameterAverage());
                    row.createCell(40).setCellValue(p.getOuterDiameterRange());
                    row.createCell(41).setCellValue(p.getOuterDiameterRoundness());
                    row.createCell(42).setCellValue(p.getOuterDiameterShrinkage());
                    row.createCell(43).setCellValue(p.getOuterDiameterRoughness());
                    row.createCell(44).setCellValue(p.getR1Flatness());
                    row.createCell(45).setCellValue(p.getR2Flatness());
                    row.createCell(46).setCellValue(p.getR1SplitAverage());
                    row.createCell(47).setCellValue(p.getR2SplitAverage());
                    row.createCell(48).setCellValue(p.getWftStability());
                    row.createCell(49).setCellValue(p.getWftConsistency());
                    row.createCell(50).setCellValue(p.getWftMaxAs());
                    row.createCell(51).setCellValue(p.getWftOuterDiameterShrinkage());
                    row.createCell(52).setCellValue(p.getCftR1());
                    row.createCell(53).setCellValue(p.getCftR2());
                    row.createCell(54).setCellValue(p.getCftConsistency());
                    row.createCell(55).setCellValue(p.getCftMaxAs());
                    row.createCell(56).setCellValue(p.getCoatingTrend());
                    row.createCell(57).setCellValue(p.getCfsrR1());
                    row.createCell(58).setCellValue(p.getCfsrR2());
                    row.createCell(59).setCellValue(p.getCfsrR1R2());
                    row.createCell(60).setCellValue(p.getBurr());
                    row.createCell(61).setCellValue(p.getWeldline());
                    row.createCell(62).setCellValue(p.getAppearanceProblem());
                    row.createCell(63).setCellValue(p.getAppearanceImg());
                    row.createCell(64).setCellValue(p.getRemarks());

                    row.createCell(65).setCellValue(p.getCoreThicknessLens());
                    row.createCell(66).setCellValue(p.getMaxWallThickness());
                    row.createCell(67).setCellValue(p.getMinWallThickness());
                    row.createCell(68).setCellValue(p.getMaxCoreRatio());
                    row.createCell(69).setCellValue(p.getMaxMinRatio());
                    row.createCell(70).setCellValue(p.getOuterDiameter());
                    row.createCell(71).setCellValue(p.getEdgeThickness());
                    row.createCell(72).setCellValue(p.getWholeMinWallThickness());
                    row.createCell(73).setCellValue(p.getWholeMaxWallThickness());
                    row.createCell(74).setCellValue(p.getWholeMaxMinRatio());
                    row.createCell(75).setCellValue(p.getWholeDiameterThicknessRatio());
                    row.createCell(76).setCellValue(p.getMaxAngleR1());
                    row.createCell(77).setCellValue(p.getMaxAngleR2());
                    row.createCell(78).setCellValue(p.getR1R2Distance());
                    row.createCell(79).setCellValue(p.getMiddlePartThickness());
                    row.createCell(80).setCellValue(p.getBottomDiameterDistance());
                    row.createCell(81).setCellValue(p.getMechanismDiameterThicknessRatio());
                    row.createCell(82).setCellValue(p.getR1KanheAngle());
                    row.createCell(83).setCellValue(p.getR1KanheHeight());
                    row.createCell(84).setCellValue(p.getR2KanheAngle());
                    row.createCell(85).setCellValue(p.getR2KanheHeight());
                    row.createCell(86).setCellValue(p.getR1Srtm());
                    row.createCell(87).setCellValue(p.getR2Srtm());
                    row.createCell(88).setCellValue(p.getOuterDiameterSrtm());
                    row.createCell(89).setCellValue(p.getAssemblyDrawing());

                }
            }
        } catch (Exception e) {
            log.error("导出数据异常", e);
            throw e;
        }
        ExcelUtil.exportXlsx(response, wb, "关联数据.xlsx");
    }
}

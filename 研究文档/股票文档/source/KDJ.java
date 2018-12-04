/*
 * @(#)KDJAnalyser.java
 *
 * Copyright @ Hangzhou Shengren Software Tech. Co., Ltd.
 */
package com.shengrensoft.stock.model.analyser.kdj;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import com.shengrensoft.stock.center.func.Function;
import com.shengrensoft.stock.center.func.KDJ;
import com.shengrensoft.stock.center.func.KDJ.Value;
import com.shengrensoft.stock.center.common.util.DateUtil;
import com.shengrensoft.stock.center.common.util.AnalysisUtil;
import com.shengrensoft.stock.center.common.util.AnalysisUtil.CrossStyle;
import com.shengrensoft.stock.center.model.ModelParam;
import com.shengrensoft.stock.center.model.analyser.AbstractAnalyser;
import com.shengrensoft.stock.center.model.analyser.AnalysisValue;

/**
 * <code>KDJ</code>随机分析模型。
 * 
 * @author 迅动股票分析平台 2012/09/07 新建
 */
public class KDJAnalyser extends AbstractAnalyser {

    /** 序列版本ID */
    private static final long serialVersionUID = -732447148977150017L;

    /** 为求必要的准确度，分析时自动前推若干天 */
    private static final int FORWARD_DAYS = 5; // 前推5天

    // 参数定义

    /** 参数名：N日 */
    private static final String PARAM_N = "N";

    /** 参数名：M1日 */
    private static final String PARAM_M1 = "M1";

    /** 参数名：M2日 */
    private static final String PARAM_M2 = "M2";
    
    /** 参数名：高位 */
    private static final String PARAM_HIGH = "HIGH";

    /** 参数名：低位 */
    private static final String PARAM_LOW = "LOW";

    /**
     * 构造方法。
     */
    public KDJAnalyser() throws RemoteException {}

    /**
     * 对市场、板块或股票进行量化分析。
     * 
     * @param stkCode    证券代码
     * @param paramsList 模型参数列表
     * 
     * @return 分析结果（含评级和评价）
     */
    public AnalysisValue doAnalysis(
            String stkCode,
            List<ModelParam> paramsList) throws RemoteException {

        // 股票分析结果
        AnalysisValue analysisValue = new AnalysisValue();
        analysisValue.setStkCode(stkCode);

        // 参数列表合法性检查
        if (paramsList == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("参数列表不合法，分析失败。");
            return analysisValue;
        }

        // 取得分析所需的参数
        int paramN = 0;    // 参数名：N日
        int paramM1 = 0;   // 参数名：M1日
        int paramM2 = 0;   // 参数名：M2日
        int paramHIGH = 0; // 参数名：高位
        int paramLOW = 0;  // 参数名：低位
        for (ModelParam param : paramsList) {
            if (PARAM_N.equals(param.getShortName())) {
                paramN = NumberUtils.toInt(param.getValue());
            } else if (PARAM_M1.equals(param.getShortName())) {
                paramM1 = NumberUtils.toInt(param.getValue());
            } else if (PARAM_M2.equals(param.getShortName())) {
                paramM2 = NumberUtils.toInt(param.getValue());
            } else if (PARAM_HIGH.equals(param.getShortName())) {
                paramHIGH = NumberUtils.toInt(param.getValue());
            } else if (PARAM_LOW.equals(param.getShortName())) {
                paramLOW = NumberUtils.toInt(param.getValue());
            } else {
                // 提供了不合法的参数
                analysisValue.setResultScore(0);
                analysisValue.setResultDesc("提供了不合法的参数，分析失败。");
                return analysisValue;
            }
        }

        // N日的合法性检查（一般为10天）
        if (paramN < 1 || paramN > 99) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("N日的参数值不合法，分析停止。");
            return analysisValue;
        }

        // 高位的合法性检查
        if (paramHIGH < 0 || paramHIGH > 200) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("高位的参数值不合法，分析停止。");
            return analysisValue;
        }

        // 低位的合法性检查
        if (paramLOW < 0 || paramLOW > 200) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("低位的参数值不合法，分析停止。");
            return analysisValue;
        }

        // 低位不得高于高位的合法性检查
        if (paramLOW > paramHIGH) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("低位的参数值不能高于高位，分析停止。");
            return analysisValue;
        }

        // 为达到必要的分析精度，前推 N 日
        Date today = null; // 当前可交易日
        java.util.Date beginDate = null;
        if (stkCode == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("证券代码不合法，分析失败。");
            return analysisValue;
        } else if (super.getStockUtil().isStkCode(stkCode)) {
            today = super.getStkTransDateUtil().getCurrentTransDate(
                    stkCode);
            beginDate = super.getStkTransDateUtil().getBeforeTransDate(
                    stkCode, today, FORWARD_DAYS);
        } else if (super.getStockUtil().isIdxCode(stkCode)) {
            today = super.getStkTransDateUtil().getCurrentTransDate();
            beginDate = super.getStkTransDateUtil().getBeforeTransDate(
                    today, FORWARD_DAYS);
        } else if (super.getStockUtil().isBlkCode(super.getUserId(), stkCode)) {
            today = super.getStkTransDateUtil().getCurrentTransDate();
            beginDate = super.getStkTransDateUtil().getBeforeTransDate(
                    today, FORWARD_DAYS);
        } else {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("证券代码不合法，分析失败。");
            return analysisValue;
        }
        if (today == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("交易日期不合法，分析失败。");
            return analysisValue;
        }
        if (beginDate == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("交易日期不合法，分析失败。");
            return analysisValue;
        }
        String beginDateStr = DateUtil.getFormatedDate(beginDate, "yyyyMMdd");
        String closeDateStr = DateUtil.getFormatedDate(today, "yyyyMMdd");

        // 取得KDJ的值序列
        KDJ funcKDJ = (KDJ) super.getFunctionGenerator().generate(Function.Name.KDJ, super.getUserId());
        List<Value> kdjList = funcKDJ.getDailyValueLs(
                stkCode,
                beginDateStr,
                closeDateStr,
                paramN,
                paramM1,
                paramM2);
        if (kdjList == null || kdjList.size() == 0) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("或因新股等原因，数据不足，未能成功分析。");
            return analysisValue;
        }

        // 返回实际的股票分析结果
        return assessValue(stkCode, kdjList, paramLOW * 100, paramHIGH * 100);

    }

    /**
     * 分析KDJ的趋势变化，作出结果判定。
     * 
     * @param stkCode   证券代码
     * @param kdjList   KDJ值序列
     * @param paramHIGH 参数：高位
     * @param paramLOW  参数：低位
     * 
     * @return 分析结果（评分和评价）
     */
    private AnalysisValue assessValue(
            String stkCode,
            List<Value> kdjList,
            int paramHIGH,
            int paramLOW) {

        // 股票分析结果
        AnalysisValue analysisValue = new AnalysisValue();
        analysisValue.setStkCode(stkCode);

        // 前日的KDJ值
        Value beforeValue = kdjList.get(kdjList.size() - 2);
        if (beforeValue == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("或因新股等原因，数据不足，未能成功分析。");
        }
        Integer beforeValueK = beforeValue.getK(); // 前日的K值
        Integer beforeValueD = beforeValue.getD(); // 前日的D值
        //Integer beforeValueJ = beforeValue.getJ(); // 前日的J值

        // 当日的KDJ值
        Value currentValue = kdjList.get(kdjList.size() - 1);
        if (currentValue == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("或因新股等原因，数据不足，未能成功分析。");
        }
        Integer currentValueK = currentValue.getK(); // 当日的K值
        Integer currentValueD = currentValue.getD(); // 当日的D值
        Integer currentValueJ = currentValue.getJ(); // 当日的J值

        // 判定K和D两个值序列的接近、偏离、或交叉的形态
        CrossStyle style = AnalysisUtil.assessCrossStyle(
                beforeValueK,
                currentValueK,
                beforeValueD,
                currentValueD);

        if (currentValueK <= paramLOW 
                && currentValueD <= paramLOW 
                && currentValueJ <= paramLOW) {

            if (style == CrossStyle.UpCross){

                // K、D、J值均在超卖的低位（一般是20以下），且K和D发生了金叉，强烈建议买入
                analysisValue.setResultScore(90);
                analysisValue.setResultDesc("K、D、J值均在超卖的低位，且K和D发生了金叉，强烈建议买入。");
                return analysisValue;

            } else {

                // K、D、J值均在超卖的低位（一般是20以下），但K和D未发生金叉，建议买入
                analysisValue.setResultScore(80);
                analysisValue.setResultDesc("K、D、J值均在超卖的低位，但K和D未发生金叉，建议买入。");
                return analysisValue;

            }

        } else if (paramLOW < currentValueK && currentValueK < paramHIGH 
                && paramLOW < currentValueD && currentValueD < paramHIGH 
                && paramLOW < currentValueJ && currentValueJ < paramHIGH) {

            // K、D、J值均在徘徊区间（一般是20-80之间），静观其变
            analysisValue.setResultScore(50);
            analysisValue.setResultDesc("K、D、J值均在徘徊区间，静观其变。");
            return analysisValue;

        } else if (currentValueK >= paramHIGH 
                && currentValueD >= paramHIGH 
                && currentValueJ >= paramHIGH) {

            if (style == CrossStyle.DownCross) {

                // K、D、J值均在超买的高位（一般是80以上），且K和D发生了死叉，强烈建议卖出
                analysisValue.setResultScore(10);
                analysisValue.setResultDesc("K、D、J值均在超买的高位，且K和D发生了死叉，强烈建议卖出。");
                return analysisValue;

            } else {

                // K、D、J值均在超买的高位（一般是80以上），但K和D未发生死叉，建议卖出
                analysisValue.setResultScore(20);
                analysisValue.setResultDesc("K、D、J值均在超买的高位，但K和D未发生死叉，建议卖出。");
                return analysisValue;

            }

        } else {

            if (currentValueD <= paramLOW  || currentValueJ <= paramLOW) {

                // D值或J值在超卖的低位（一般是20以下），可考虑买入
                analysisValue.setResultScore(70);
                analysisValue.setResultDesc("D值或J值在超卖的低位，可考虑买入。");
                return analysisValue;

            } else if (currentValueD >= paramHIGH  
                    || currentValueJ >= paramHIGH) {

                // D值或J值在超买的高位（一般是80以上），可考虑卖出
                analysisValue.setResultScore(30);
                analysisValue.setResultDesc("D值或J值在超买的高位，可考虑卖出。");
                return analysisValue;

            } else {

                // 其它情况
                analysisValue.setResultScore(0);
                analysisValue.setResultDesc("情况不很明朗，请留意风险。");
                return analysisValue;

            }

        }

     }

}

/*
 
 原理：
   用目前股价在近阶段股价分布中的相对位置来预测可能发生的趋势反转。

 算法：
   对每一交易日求RSV(未成熟随机值)
   RSV=(收盘价－最近N日最低价)/(最近N日最高价－最近N日最低价)×100
   K线：RSV的M1日移动平均
   D线：K值的M2日移动平均
   J线：3×D－2×K

 参数：
   N、M1、M2 天数，一般取9、3、3

 用法：
    1.D>80，超买；D<20，超卖；J>100%超卖；J<10%超卖
    2.线K向上突破线D，买进信号；线K向下跌破线D，卖出信号。
    3.线K与线D的交叉发生在70以上，30以下，才有效。
    4.KD指标不适于发行量小，交易不活跃的股票；
    5.KD指标对大盘和热门大盘股有极高准确性。

*/

/*

 RSV=(收盘价－最近N日最低价)/(最近N日最高价－
     最近N日最低价)×100
 K线：RSV的M1日移动平均   
 D线：K值的M2日移动平均
 J线：3×D－2×K

*/

// 迅动股票分析平台 www.xdstock.com 程序化交易平台 杭州胜人软件技术有限公司 版权所有
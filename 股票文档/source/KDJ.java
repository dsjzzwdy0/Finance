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
 * <code>KDJ</code>�������ģ�͡�
 * 
 * @author Ѹ����Ʊ����ƽ̨ 2012/09/07 �½�
 */
public class KDJAnalyser extends AbstractAnalyser {

    /** ���а汾ID */
    private static final long serialVersionUID = -732447148977150017L;

    /** Ϊ���Ҫ��׼ȷ�ȣ�����ʱ�Զ�ǰ�������� */
    private static final int FORWARD_DAYS = 5; // ǰ��5��

    // ��������

    /** ��������N�� */
    private static final String PARAM_N = "N";

    /** ��������M1�� */
    private static final String PARAM_M1 = "M1";

    /** ��������M2�� */
    private static final String PARAM_M2 = "M2";
    
    /** ����������λ */
    private static final String PARAM_HIGH = "HIGH";

    /** ����������λ */
    private static final String PARAM_LOW = "LOW";

    /**
     * ���췽����
     */
    public KDJAnalyser() throws RemoteException {}

    /**
     * ���г��������Ʊ��������������
     * 
     * @param stkCode    ֤ȯ����
     * @param paramsList ģ�Ͳ����б�
     * 
     * @return ��������������������ۣ�
     */
    public AnalysisValue doAnalysis(
            String stkCode,
            List<ModelParam> paramsList) throws RemoteException {

        // ��Ʊ�������
        AnalysisValue analysisValue = new AnalysisValue();
        analysisValue.setStkCode(stkCode);

        // �����б�Ϸ��Լ��
        if (paramsList == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("�����б��Ϸ�������ʧ�ܡ�");
            return analysisValue;
        }

        // ȡ�÷�������Ĳ���
        int paramN = 0;    // ��������N��
        int paramM1 = 0;   // ��������M1��
        int paramM2 = 0;   // ��������M2��
        int paramHIGH = 0; // ����������λ
        int paramLOW = 0;  // ����������λ
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
                // �ṩ�˲��Ϸ��Ĳ���
                analysisValue.setResultScore(0);
                analysisValue.setResultDesc("�ṩ�˲��Ϸ��Ĳ���������ʧ�ܡ�");
                return analysisValue;
            }
        }

        // N�յĺϷ��Լ�飨һ��Ϊ10�죩
        if (paramN < 1 || paramN > 99) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("N�յĲ���ֵ���Ϸ�������ֹͣ��");
            return analysisValue;
        }

        // ��λ�ĺϷ��Լ��
        if (paramHIGH < 0 || paramHIGH > 200) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("��λ�Ĳ���ֵ���Ϸ�������ֹͣ��");
            return analysisValue;
        }

        // ��λ�ĺϷ��Լ��
        if (paramLOW < 0 || paramLOW > 200) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("��λ�Ĳ���ֵ���Ϸ�������ֹͣ��");
            return analysisValue;
        }

        // ��λ���ø��ڸ�λ�ĺϷ��Լ��
        if (paramLOW > paramHIGH) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("��λ�Ĳ���ֵ���ܸ��ڸ�λ������ֹͣ��");
            return analysisValue;
        }

        // Ϊ�ﵽ��Ҫ�ķ������ȣ�ǰ�� N ��
        Date today = null; // ��ǰ�ɽ�����
        java.util.Date beginDate = null;
        if (stkCode == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("֤ȯ���벻�Ϸ�������ʧ�ܡ�");
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
            analysisValue.setResultDesc("֤ȯ���벻�Ϸ�������ʧ�ܡ�");
            return analysisValue;
        }
        if (today == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("�������ڲ��Ϸ�������ʧ�ܡ�");
            return analysisValue;
        }
        if (beginDate == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("�������ڲ��Ϸ�������ʧ�ܡ�");
            return analysisValue;
        }
        String beginDateStr = DateUtil.getFormatedDate(beginDate, "yyyyMMdd");
        String closeDateStr = DateUtil.getFormatedDate(today, "yyyyMMdd");

        // ȡ��KDJ��ֵ����
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
            analysisValue.setResultDesc("�����¹ɵ�ԭ�����ݲ��㣬δ�ܳɹ�������");
            return analysisValue;
        }

        // ����ʵ�ʵĹ�Ʊ�������
        return assessValue(stkCode, kdjList, paramLOW * 100, paramHIGH * 100);

    }

    /**
     * ����KDJ�����Ʊ仯����������ж���
     * 
     * @param stkCode   ֤ȯ����
     * @param kdjList   KDJֵ����
     * @param paramHIGH ��������λ
     * @param paramLOW  ��������λ
     * 
     * @return ������������ֺ����ۣ�
     */
    private AnalysisValue assessValue(
            String stkCode,
            List<Value> kdjList,
            int paramHIGH,
            int paramLOW) {

        // ��Ʊ�������
        AnalysisValue analysisValue = new AnalysisValue();
        analysisValue.setStkCode(stkCode);

        // ǰ�յ�KDJֵ
        Value beforeValue = kdjList.get(kdjList.size() - 2);
        if (beforeValue == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("�����¹ɵ�ԭ�����ݲ��㣬δ�ܳɹ�������");
        }
        Integer beforeValueK = beforeValue.getK(); // ǰ�յ�Kֵ
        Integer beforeValueD = beforeValue.getD(); // ǰ�յ�Dֵ
        //Integer beforeValueJ = beforeValue.getJ(); // ǰ�յ�Jֵ

        // ���յ�KDJֵ
        Value currentValue = kdjList.get(kdjList.size() - 1);
        if (currentValue == null) {
            analysisValue.setResultScore(0);
            analysisValue.setResultDesc("�����¹ɵ�ԭ�����ݲ��㣬δ�ܳɹ�������");
        }
        Integer currentValueK = currentValue.getK(); // ���յ�Kֵ
        Integer currentValueD = currentValue.getD(); // ���յ�Dֵ
        Integer currentValueJ = currentValue.getJ(); // ���յ�Jֵ

        // �ж�K��D����ֵ���еĽӽ���ƫ�롢�򽻲����̬
        CrossStyle style = AnalysisUtil.assessCrossStyle(
                beforeValueK,
                currentValueK,
                beforeValueD,
                currentValueD);

        if (currentValueK <= paramLOW 
                && currentValueD <= paramLOW 
                && currentValueJ <= paramLOW) {

            if (style == CrossStyle.UpCross){

                // K��D��Jֵ���ڳ����ĵ�λ��һ����20���£�����K��D�����˽�棬ǿ�ҽ�������
                analysisValue.setResultScore(90);
                analysisValue.setResultDesc("K��D��Jֵ���ڳ����ĵ�λ����K��D�����˽�棬ǿ�ҽ������롣");
                return analysisValue;

            } else {

                // K��D��Jֵ���ڳ����ĵ�λ��һ����20���£�����K��Dδ������棬��������
                analysisValue.setResultScore(80);
                analysisValue.setResultDesc("K��D��Jֵ���ڳ����ĵ�λ����K��Dδ������棬�������롣");
                return analysisValue;

            }

        } else if (paramLOW < currentValueK && currentValueK < paramHIGH 
                && paramLOW < currentValueD && currentValueD < paramHIGH 
                && paramLOW < currentValueJ && currentValueJ < paramHIGH) {

            // K��D��Jֵ�����ǻ����䣨һ����20-80֮�䣩���������
            analysisValue.setResultScore(50);
            analysisValue.setResultDesc("K��D��Jֵ�����ǻ����䣬������䡣");
            return analysisValue;

        } else if (currentValueK >= paramHIGH 
                && currentValueD >= paramHIGH 
                && currentValueJ >= paramHIGH) {

            if (style == CrossStyle.DownCross) {

                // K��D��Jֵ���ڳ���ĸ�λ��һ����80���ϣ�����K��D���������棬ǿ�ҽ�������
                analysisValue.setResultScore(10);
                analysisValue.setResultDesc("K��D��Jֵ���ڳ���ĸ�λ����K��D���������棬ǿ�ҽ���������");
                return analysisValue;

            } else {

                // K��D��Jֵ���ڳ���ĸ�λ��һ����80���ϣ�����K��Dδ�������棬��������
                analysisValue.setResultScore(20);
                analysisValue.setResultDesc("K��D��Jֵ���ڳ���ĸ�λ����K��Dδ�������棬����������");
                return analysisValue;

            }

        } else {

            if (currentValueD <= paramLOW  || currentValueJ <= paramLOW) {

                // Dֵ��Jֵ�ڳ����ĵ�λ��һ����20���£����ɿ�������
                analysisValue.setResultScore(70);
                analysisValue.setResultDesc("Dֵ��Jֵ�ڳ����ĵ�λ���ɿ������롣");
                return analysisValue;

            } else if (currentValueD >= paramHIGH  
                    || currentValueJ >= paramHIGH) {

                // Dֵ��Jֵ�ڳ���ĸ�λ��һ����80���ϣ����ɿ�������
                analysisValue.setResultScore(30);
                analysisValue.setResultDesc("Dֵ��Jֵ�ڳ���ĸ�λ���ɿ���������");
                return analysisValue;

            } else {

                // �������
                analysisValue.setResultScore(0);
                analysisValue.setResultDesc("����������ʣ���������ա�");
                return analysisValue;

            }

        }

     }

}

/*
 
 ԭ��
   ��Ŀǰ�ɼ��ڽ��׶ιɼ۷ֲ��е����λ����Ԥ����ܷ��������Ʒ�ת��

 �㷨��
   ��ÿһ��������RSV(δ�������ֵ)
   RSV=(���̼ۣ����N����ͼ�)/(���N����߼ۣ����N����ͼ�)��100
   K�ߣ�RSV��M1���ƶ�ƽ��
   D�ߣ�Kֵ��M2���ƶ�ƽ��
   J�ߣ�3��D��2��K

 ������
   N��M1��M2 ������һ��ȡ9��3��3

 �÷���
    1.D>80������D<20��������J>100%������J<10%����
    2.��K����ͻ����D������źţ���K���µ�����D�������źš�
    3.��K����D�Ľ��淢����70���ϣ�30���£�����Ч��
    4.KDָ�겻���ڷ�����С�����ײ���Ծ�Ĺ�Ʊ��
    5.KDָ��Դ��̺����Ŵ��̹��м���׼ȷ�ԡ�

*/

/*

 RSV=(���̼ۣ����N����ͼ�)/(���N����߼ۣ�
     ���N����ͼ�)��100
 K�ߣ�RSV��M1���ƶ�ƽ��   
 D�ߣ�Kֵ��M2���ƶ�ƽ��
 J�ߣ�3��D��2��K

*/

// Ѹ����Ʊ����ƽ̨ www.xdstock.com ���򻯽���ƽ̨ ����ʤ������������޹�˾ ��Ȩ����
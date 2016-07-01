package com.lv.test;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.Map;


public class WebServiceUtil {

    private Boolean _isdotnet = false;

    /*
     * 设置当前WebServices是否支持 .net 的WebServices；
     * @param dotNetWebService:  .net默认true;java默认是false
     */
    public WebServiceUtil setIsDotNet(boolean dotNetWebService) {
        _isdotnet = dotNetWebService;
        return this;
    }

    private int _setHttpTimeOut = 10 * 1000;

    /*
     * 设置HTTP请求的时间，单位：秒；
     * @param secondTime: 默认 10 s
     */
    public WebServiceUtil setHttpTimeOut(int secondTime) {
        _setHttpTimeOut = secondTime;
        return this;
    }

    private boolean _isdebug = false;

    /*
     * 设置启用HTTP的Debug模式
     * @param isdebug: 默认 false
     */
    public WebServiceUtil setIsDebug(boolean isdebug) {
        _isdebug = isdebug;
        return this;
    }


    /*
     * 获取WebService数据，并以字符形式返回。
     * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
     * @param MethodName: WebService的调用函数方法名称(getDataMethod)
     * @param Maps: 请求服务需要提交的数据集
     * @Return: 服务以字符类型返回请求数据
     * @Exception: 写入控制台日志
     */
    public String getString(String Url, String NameSpace, String MethodName, Map<String, ?> RequestDatas) {
        return getString(Url, NameSpace, MethodName, RequestDatas, null, null);
    }

    /*
     * 获取WebService数据，并以字符形式返回。
     * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
     * @param MethodName: WebService的调用函数方法名称(getDataMethod)
     * @param Maps: 请求服务需要提交的数据集
     * @Return: 服务以字符类型返回请求数据
     * @Exception: 写入控制台日志
     */
    public String getString(String Url, String NameSpace, String MethodName) {
        return getString(Url, NameSpace, MethodName, null, null, null);
    }

    /*
     * 获取WebService数据，并以字符形式返回。
     * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
     * @param MethodName: WebService的调用函数方法名称(getDataMethod)
     * @param Maps: 请求服务需要提交的数据集
     * @param SoapHeadeName: 设置WebService的HTTP头名称
     * @param SoapHeadeValues: 设置 SoapHeade 的数据集
     * @Return: 服务以字符类型返回请求数据
     * @Exception: 写入控制台日志
     */
    public String getString(String Url, String NameSpace, String MethodName, Map<String, ?> RequestDatas, String SoapHeadeName, Map<String, ?> SoapHeadeValues) {
        SoapObject soap = getObject(Url, NameSpace, MethodName, RequestDatas, SoapHeadeName, SoapHeadeValues);
        if (soap != null && soap.getPropertyCount() > 0) {
            String getResultString = soap.getProperty(0).toString();
            return getResultString;
        }
        return null;
    }

    /*
     * 获取WebService数据，返回SoapObject对象。
     * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
     * @param MethodName: WebService的调用函数方法名称(getDataMethod)
     * @param Maps: 请求服务需要提交的数据集
     * @Return: 服务返回SoapObject对象
     * @Exception: 写入控制台日志
     */
    public SoapObject getObject(String Url, String NameSpace, String MethodName, Map<String, Object> RequestDatas) {
        return getObject(Url, NameSpace, MethodName, RequestDatas, null, null);
    }

    /*
     * 获取WebService数据，返回SoapObject对象。
     * @param Url: WebService服务地址 (http://webservice.***.com.cn/WeatherWS.asmx)
     * @param NameSpace: WebService的服务的命名空间，可以WSDL数据中找到 (http://***.com.cn/)
     * @param MethodName: WebService的调用函数方法名称(getDataMethod)
     * @param Maps: 请求服务需要提交的数据集
     * @param SoapHeadeName: 设置WebService的HTTP头名称
     * @param SoapHeadeValues: 设置 SoapHeade 的数据集
     * @Return: 服务返回SoapObject对象
     * @Exception: 写入控制台日志
     */
    public SoapObject getObject(String Url, String NameSpace, String MethodName, Map<String, ?> RequestDatas, String SoapHeadeName, Map<String, ?> SoapHeadeValues) {
        try {

            SoapObject soap = new SoapObject(NameSpace, MethodName);

            // 设置WebService提交的数据集
            if (RequestDatas != null && !RequestDatas.isEmpty()) {
                for (Map.Entry<String, ?> entry : RequestDatas.entrySet()) {
                    soap.addProperty(entry.getKey(), entry.getValue());
                }
            }

            // 设置HTTP头信息
            Element[] header = null;
            if (SoapHeadeName != null && SoapHeadeValues != null && !SoapHeadeValues.isEmpty()) {
                header = new Element[1];
                header[0] = new Element().createElement(NameSpace, SoapHeadeName);

                for (Map.Entry<String, ?> entry : SoapHeadeValues.entrySet()) {
                    Element element = new Element().createElement(NameSpace, entry.getKey());
                    element.addChild(Node.TEXT, entry.getValue());
                    header[0].addChild(Node.ELEMENT, element);
                }
            }

            // 初始化数据请求
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = _isdotnet;
            if (header != null) envelope.headerOut = header;
            envelope.bodyOut = soap;
            envelope.setOutputSoapObject(soap);

            // 发起Web请求
            HttpTransportSE http = new HttpTransportSE(Url, _setHttpTimeOut);
            http.debug = _isdebug;
            http.call(NameSpace + MethodName, envelope);

            // 获取Web请求结果， 数据需要从 result.getProperty(0) 获取
            SoapObject result = (SoapObject) envelope.bodyIn;


            return result;

        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }


}
package com.dagger4j.mvc.http.decoder;

import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.upload.FileItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.*;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * Created by laotang on 2017/10/31.
 */
public class MultiPartPostDecoder extends AbstractDecoder<Map<String,Object>> {


    public MultiPartPostDecoder(FullHttpRequest request) {
        super(request);
    }

    @Override
    public Map<String, Object> decoder() throws Exception {
        HttpPostMultipartRequestDecoder requestDecoder = new HttpPostMultipartRequestDecoder(HTTP_DATA_FACTORY, request);
        List<InterfaceHttpData> paramsList = requestDecoder.getBodyHttpDatas();
        if (null != paramsList && !paramsList.isEmpty()) {
            for (InterfaceHttpData httpData : paramsList) {
                InterfaceHttpData.HttpDataType dataType = httpData.getHttpDataType();
                if(dataType == InterfaceHttpData.HttpDataType.Attribute
                    || dataType == InterfaceHttpData.HttpDataType.InternalAttribute) {
                    setValue2ParamMap(httpData);
                } else if(dataType == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUpload fileUpload = (FileUpload) httpData;
                    if (null != fileUpload && fileUpload.isCompleted()) {
                        FileItem fileItem = null;
                        byte[] bytes = null;
                        if (fileUpload.isInMemory()) {
                            ByteBuf byteBuf = fileUpload.getByteBuf();
                            bytes = ByteBufUtil.getBytes(byteBuf);
                        } else {
                            bytes = Files.readAllBytes(fileUpload.getFile().toPath());
                        }
                        fileItem = new FileItem(fileUpload.getName(), fileUpload.getContentTransferEncoding(), fileUpload.getFilename(), fileUpload.getContentType(), fileUpload.getFile().length(), bytes);
                        attributeMap.put(fileItem.getName(), fileItem);
                    }
                }
            }
        }
        return attributeMap;
    }

    private void setValue2ParamMap(InterfaceHttpData httpData) throws Exception {
        MixedAttribute attribute = (MixedAttribute) httpData;
        String key = attribute.getName();
        String value = attribute.getValue();
        if(ToolsKit.isNotEmpty(value)) {
            attributeMap.put(key, value);
        }
    }
}

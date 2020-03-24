package com.wsy.testuiapplication.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.wsy.testuiapplication.util.Slog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class MultipartRequest extends Request {
    private final Gson mGson = new Gson();
    private final Class mClazz;
    private final String TAG = "MultipartRequest";
    private final String mBoundary = "boundary-test";
    private static final String LINE_END = "\r\n";

    private final ImageUploadManager.Listener mListener;

    private final Map mHeaders;

    private final String mMimeType;

    private final byte[] mMultipartBody;

    public MultipartRequest(int method, String url, Class clazz, Map headers, String mimeType, byte[] multipartBody,
                            ImageUploadManager.Listener listener) {

        super(method, url, createErrorListener(listener));

        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
        this.mMimeType = mimeType;
        this.mMultipartBody = multipartBody;

    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();

    }

    @Override
    public String getBodyContentType() {
        return mMimeType;
    }

    @Override

    public byte[] getBody() throws AuthFailureError {
//        return mMultipartBody;
        return createMultipart();
    }

    @Override
    protected void deliverResponse(Object response) {
        if (mListener != null) {
            mListener.onSuccess(response);
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(json, mClazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }


    private byte[] createMultipart() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
//            buildMultiPartTextPart(dos);
            buildMultiPartDataPart(dos);
            dos.writeBytes("--" + mBoundary + "--" + LINE_END);
        } catch (Exception e) {
            Slog.e(TAG, e.getMessage());
        }

        return bos.toByteArray();
    }


//    private void buildMultiPartTextPart(DataOutputStream dos) throws IOException {
//        for (HashMap.Entry<String, String> entry : mParams.entrySet()) {
//            dos.writeBytes("--" + mBoundary + LINE_END);
//            dos.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
//            dos.writeBytes(LINE_END);
//            dos.write((entry.getValue() + LINE_END).getBytes("UTF-8"));
//        }
//    }

    private void buildMultiPartDataPart(DataOutputStream dos) throws IOException {
//        if (mDataParts == null) {
//            return;
//        }
        DFDataPart part = new DFDataPart("avatar", "icons.png", mMimeType, mMultipartBody);
//        for (DFDataPart part : mDataParts) {
        dos.writeBytes("--" + mBoundary + LINE_END);
        dos.writeBytes(
                "Content-Disposition: form-data; name=\"" + part.name + "\"; " + "filename=\"" + part.filename + "\"" +
                LINE_END);
        dos.writeBytes("Content-Type: " + part.mimeType + LINE_END);
        dos.writeBytes(LINE_END);
        dos.write(part.data);
        dos.writeBytes(LINE_END);
//        }
    }


    private class DFDataPart {

        public DFDataPart(String name, String filename, String mimeType, byte[] data) {
            this.name = name;
            this.filename = filename;
            this.mimeType = mimeType;
            this.data = data;
        }

        public String name;
        public String filename;
        public String mimeType;
        public byte[] data;
    }


    private static Response.ErrorListener createErrorListener(final ImageUploadManager.Listener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "";
                if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                    Slog.e("MultipartRequest", "volley error" + errorMessage);
                }
//                int errorCode = parseError(error);

                if (listener != null) {
                    listener.onFail(errorMessage);
                }
            }
        };
    }
}

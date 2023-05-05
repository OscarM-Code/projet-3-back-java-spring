package com.projetjavaopc.api.tools.response;

import com.projetjavaopc.api.tools.specialModel.BasicResponse;

import org.springframework.stereotype.Repository;

@Repository
public class ResponseProvider {


    public BasicResponse response(String message, Object data) {
        BasicResponse response = new BasicResponse();

        response.setMessage(message);
        response.setData(data);

        return response;
    }
    
}

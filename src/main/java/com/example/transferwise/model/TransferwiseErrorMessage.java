package com.example.transferwise.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferwiseErrorMessage {
    private String statusCode;
    private String cause;

    public static TransferwiseErrorMessage fromResponseJson(String jsonString){
        try{
            ObjectMapper om = new ObjectMapper();
            JsonNode jsonNode = om.valueToTree(jsonString);
            String statusCode = jsonNode.get("statusCode").textValue();
            String cause = jsonNode.get("cause").textValue();
            return new TransferwiseErrorMessage(statusCode, cause);
        }catch (Exception e){
            return new TransferwiseErrorMessage("INVALID", "Could not read response string");

        }
    }


}

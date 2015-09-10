/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jaques
 * sintax: input operator expectValue: output = actionValue
 * example: ldr <= 200: luz = 1 
 */
@Entity
public class Job extends AbstractEntityId {
    
    @ManyToOne
    private DeviceConfig input;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date inputDate;
    
    private JobOperator operator;
    private String expectValue;
    private String actionValue;
    
    private boolean enabled;
    private int index;
    
    @ManyToOne
    private DeviceConfig output;
    
    private boolean deleteAfterExecute;

    public Object getExpectValueAsType(Class clazz) {
        return castValue(clazz, expectValue);
    }
    
    public Object getActionValueAsType(Class clazz) {
        return castValue(clazz, actionValue);
    }
    
    private Object castValue(Class clazz, String value) {
        if (Boolean.class == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz)    return Byte.parseByte(value);
        if (Short.class == clazz)   return Short.parseShort(value);
        if (Integer.class == clazz) return Integer.parseInt(value);
        if (Long.class == clazz)    return Long.parseLong(value);
        if (Float.class == clazz)   return Float.parseFloat(value);
        if (Double.class == clazz)  return Double.parseDouble(value);
        return value;
    }
    
    public String getInputDescription() {
        if (input != null && operator != null && expectValue != null) {
            return input.getName() + " " + operator.toString() + " " + expectValue;
        }
        if (inputDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(inputDate);
        }
        return null;
    }
    
    public String getOutputDescription() {
        if (output != null) {
            return output.getName() + " = " + actionValue;
        }
        return null;
    }
    
    public DeviceConfig getInput() {
        return input;
    }

    public void setInput(DeviceConfig input) {
        this.input = input;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }

    public JobOperator getOperator() {
        return operator;
    }

    public void setOperator(JobOperator operator) {
        this.operator = operator;
    }

    public String getExpectValue() {
        return expectValue;
    }

    public void setExpectValue(String expectValue) {
        this.expectValue = expectValue;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public DeviceConfig getOutput() {
        return output;
    }

    public void setOutput(DeviceConfig output) {
        this.output = output;
    }

    public boolean isDeleteAfterExecute() {
        return deleteAfterExecute;
    }

    public void setDeleteAfterExecute(boolean deleteAfterExecute) {
        this.deleteAfterExecute = deleteAfterExecute;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Input: " + getInputDescription() + " | Output: " + getOutputDescription();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

/**
 *
 * @author jaques
 */
public enum JobOperator {   

    EQUAL("=") {
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) == 0;
        }
    },
    
    DIFERENT("<>") {
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) != 0;
        }
    },
    
    GREATHER(">") {       
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) > 0;
        }
    },
    
    GREATHER_EQUAL(">=") {        
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) >= 0;
        }
    },
    
    LESS("<") {
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) < 0;
        }
    },
    
    LESS_EQUAL("<=") {
        @Override
        public boolean compare(Comparable input, Object expect) {
            return input.compareTo(expect) <= 0;
        }
    };
    
    private final String symbol;
    
    JobOperator(String symbol) {
        this.symbol = symbol;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
    
    public abstract boolean compare(Comparable input, Object expect);

}

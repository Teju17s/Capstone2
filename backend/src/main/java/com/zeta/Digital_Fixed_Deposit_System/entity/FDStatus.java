package com.zeta.Digital_Fixed_Deposit_System.entity;

/**
 * Status of a Fixed Deposit account
 */
public enum FDStatus {
    /**
     * Fixed deposit is currently active and earning interest
     */
    ACTIVE,
    
    /**
     * Fixed deposit has reached maturity date
     */
    MATURED,
    
    /**
     * Fixed deposit has been closed/withdrawn by user
     */
    BROKEN
}

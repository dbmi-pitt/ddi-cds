package edu.pitt.dbmi.ohdsiv5.db;

import java.util.Calendar;
import java.sql.Timestamp;


/**
 * DrugExposure generated by hbm2java
 */

public class DrugExposure  {

    // Constructors
    /** default constructor */
    public DrugExposure() {
    }
    /** full constructor */
    public DrugExposure(Long drugExposureId,
            // Timestamp drugExposureStartDate,
            // Timestamp drugExposureEndDate,
            Timestamp drugExposureStartDate,
            Timestamp drugExposureEndDate,
            Long personId,
            Integer drugConceptId,
            Integer drugTypeConceptId,
            String stopReason,
            Short refills,
            Integer drugQuantity,
            Short daysSupply,
            String sig,
            Integer routeConceptId,
            String lotNumber,
            Integer providerId,
            Long visitOccurrenceId,
            String drugSourceValue,
            Integer drugSourceConceptId,
            String routeSourceValue,
            String doseUnitSourceValue,
            Integer indicationConceptId
            ) {
    this.drugExposureId = drugExposureId;
        this.drugExposureStartDate = drugExposureStartDate;
        this.drugExposureEndDate = drugExposureEndDate;
        this.personId = personId;
        this.drugConceptId = drugConceptId;
        this.drugTypeConceptId = drugTypeConceptId;
        this.stopReason = stopReason;
        this.refills = refills;
        this.drugQuantity = drugQuantity;
        this.daysSupply = daysSupply;
    this.sig = sig;
    
    // v5 additions
    this.routeConceptId = routeConceptId;
    this.lotNumber = lotNumber;
    this.providerId = providerId;
    this.visitOccurrenceId = visitOccurrenceId;
    this.drugSourceValue = drugSourceValue;
    this.drugSourceConceptId = drugSourceConceptId;
    this.routeSourceValue = routeSourceValue;
    this.doseUnitSourceValue = doseUnitSourceValue;

    // MDIA addition
    this.indicationConceptId = indicationConceptId;
    }
     
    // Property accessors
                    Long drugExposureId;
    public Long getDrugExposureId() {
        return this.drugExposureId;
    }
    
    public void setDrugExposureId(Long drugExposureId) {
        this.drugExposureId = drugExposureId;
    }

    //    // Timestamp drugExposureStartDate;
    // public Timestamp getDrugExposureStartDate() {
    //     return this.drugExposureStartDate;
    // }    
    // public void setDrugExposureStartDate(Timestamp drugExposureStartDate) {
    //     this.drugExposureStartDate = drugExposureStartDate;
    // }
        Timestamp drugExposureStartDate;
    public Timestamp getDrugExposureStartDate() {
        return this.drugExposureStartDate;
    }    
    public void setDrugExposureStartDate(Timestamp drugExposureStartDate) {
        this.drugExposureStartDate = drugExposureStartDate;
    }
    
    //    // Timestamp drugExposureEndDate;
    // public Timestamp getDrugExposureEndDate() {
    //     return this.drugExposureEndDate;
    // }
    // public void setDrugExposureEndDate(Timestamp drugExposureEndDate) {
    //     this.drugExposureEndDate = drugExposureEndDate;
    // }
        Timestamp drugExposureEndDate;
    public Timestamp getDrugExposureEndDate() {
        return this.drugExposureEndDate;
    }
    public void setDrugExposureEndDate(Timestamp drugExposureEndDate) {
        this.drugExposureEndDate = drugExposureEndDate;
    }

        Long personId;
    public Long getPersonId() {
        return this.personId;
    }
    public void setPersonId(Long personId) {
        this.personId = personId;
    }


        Integer drugConceptId;
    public Integer getDrugConceptId() {
        return this.drugConceptId;
    }    
    public void setDrugConceptId(Integer drugConceptId) {
        this.drugConceptId = drugConceptId;
    }

        Integer drugTypeConceptId;
    public Integer getDrugTypeConceptId() {
        return this.drugTypeConceptId;
    } 
    public void setDrugTypeConceptId(Integer drugTypeConceptId) {
        this.drugTypeConceptId = drugTypeConceptId;
    }

        String stopReason;
    public String getStopReason() {
        return this.stopReason;
    }    
    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

        Short refills;
    public Short getRefills() {
        return this.refills;
    }    
    public void setRefills(Short refills) {
        this.refills = refills;
    }

        Integer drugQuantity;
    public Integer getDrugQuantity() {
        return this.drugQuantity;
    }
    public void setDrugQuantity(Integer drugQuantity) {
        this.drugQuantity = drugQuantity;
    }

        Short daysSupply;
    public Short getDaysSupply() {
        return this.daysSupply;
    }    
    public void setDaysSupply(Short daysSupply) {
        this.daysSupply = daysSupply;
    }

        String sig;
    public String getSig() {
        return this.sig;
    }
    public void setSig(String sig) {
        this.sig = sig;
    }

    
        Integer routeConceptId;
    public Integer getRouteConceptId() {
    return this.routeConceptId;
    }
    public void setRouteConceptId(Integer routeConceptId) {
    this.routeConceptId = routeConceptId;
    }

        String lotNumber;
    public String getLotNumber() {
    return this.lotNumber;
    }
    public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
    }

        Integer providerId;
    public Integer getProviderId() {
    return this.providerId;
    }
    public void setProviderId(Integer providerId) {
    this.providerId = providerId;
    }

        Long visitOccurrenceId;
    public Long getVisitOccurrenceId() {
    return this.visitOccurrenceId;
    }
    public void setVisitOccurrenceId(Long visitOccurrenceId) {
    this.visitOccurrenceId = visitOccurrenceId;
    }

        String drugSourceValue;
    public String getDrugSourceValue() {
    return this.drugSourceValue;
    }
    public void setDrugSourceValue(String drugSourceValue) {
    this.drugSourceValue = drugSourceValue;
    }

        Integer drugSourceConceptId;
    public Integer getDrugSourceConceptId() {
    return this.drugSourceConceptId;
    }
    public void setDrugSourceConceptId(Integer drugSourceConceptId) {
    this.drugSourceConceptId = drugSourceConceptId;
    }

        String routeSourceValue;
    public String getRouteSourceValue() {
    return this.routeSourceValue;
    }
    public void setRouteSourceValue(String routeSourceValue) {
    this.routeSourceValue = routeSourceValue;
    }

        String doseUnitSourceValue;
    public String getDoseUnitSourceValue() {
    return this.doseUnitSourceValue;
    }
    public void setDoseUnitSourceValue(String doseUnitSourceValue) {
    this.doseUnitSourceValue = doseUnitSourceValue;
    }


        Integer indicationConceptId;
    public Integer getIndicationConceptId() {
    return this.indicationConceptId;
    }
    public void setIndicationConceptId(Integer indicationConceptId) {
    this.indicationConceptId = indicationConceptId;
    }

}

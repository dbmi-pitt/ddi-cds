package edu.pitt.dbmi.ohdsiv5.db;



public class SigMapping {

    // Constructor

    /** default constructor */
    public SigMapping() {
    }

    public SigMapping(
              Integer id,
              String sig,
              Integer expected,
              Integer min,
              Integer max
              ) {
    this.id = id;
    this.sig = sig;
    this.expected = expected;
    this.min = min;
    this.max = max;
    }

    // Property accessors
            Integer id;
    public Integer getSigMappingId() {
    return this.id;
    }
    public void setSigMappingId(Integer id) {
    this.id = id;
    }


        String sig;
    public String getSig() {
    return this.sig;
    }
    public void setSig(String sig) {
    this.sig = sig;
    }

        Integer expected;
    public Integer getSigExpected() {
    return this.expected;
    }
    public void setSigExpected(Integer expected) {
    this.expected = expected;
    }

        Integer min;
    public Integer getSigMin() {
    return this.min;
    }
    public void setSigMin(Integer min) {
    this.min = min;
    }

        Integer max;
    public Integer getSigMax() {
    return this.max;
    }
    public void setSigMax(Integer max) {
    this.max = max;
    }
}

package edu.pitt.dbmi.ohdsiv5.db;

import java.sql.Date;
import java.util.Calendar;
import java.sql.Timestamp;


// NOTE: not all rows are mapped


public class Person  {

	private static final long serialVersionUID = 1L;
	
	Long personId;
	Integer yearOfBirth;
	Integer monthOfBirth;
	Integer dayOfBirth;
	Integer genderCUI;
	Integer raceCUI;
	Integer locationId;
	String genderSourceCode;
	String raceSourceCode;
    Integer ethnicityCUI;
    String ethnicitySourceCode;
  Timestamp dateOfBirth;
    // Constructors

    // default constructor
    public Person() {
    }

    public Person(Long personId, Integer yearOfBirth, Integer genderCUI, Integer raceCUI, Integer ethnicityCUI) {
        this.personId = personId;
        this.yearOfBirth = yearOfBirth;
        this.genderCUI = genderCUI;
        this.raceCUI = raceCUI;
        this.ethnicityCUI = ethnicityCUI;
    }

    public Long getPersonId() {
		return personId;
    }

    public void setPersonId(Long personId) {
		this.personId = personId;
	}

  public void setDateOfBirth() {
    if (this.yearOfBirth != null && this.monthOfBirth != null && this.dayOfBirth != null) {
      Calendar b = Calendar.getInstance();
      b.set(Calendar.YEAR, this.yearOfBirth);
      b.set(Calendar.MONTH, this.monthOfBirth);
      b.set(Calendar.DAY_OF_MONTH, this.dayOfBirth);
      Timestamp dob = new Timestamp(b.getTimeInMillis());
      this.dateOfBirth = dob;
    }
  }
  public Timestamp getDateOfBirth() {
    return this.dateOfBirth;
  }

  public Double getDobDateDiffInMillis(Timestamp otherDate) {
    if (this.dateOfBirth != null) {
      double diff = (otherDate.getTime() - this.dateOfBirth.getTime());
      return diff;
    }
    else return null;
  }

    public Integer getYearOfBirth() {
		return yearOfBirth;
	}

     public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	
    public Integer getMonthOfBirth() {
		return monthOfBirth;
	}

    public void setMonthOfBirth(Integer monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	
    public Integer getDayOfBirth() {
		return dayOfBirth;
	}

    public void setDayOfBirth(Integer dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}	
	
    public Integer getGenderCUI() {
		return genderCUI;
	}
	
    public void setGenderCUI(Integer genderCUI) {
		this.genderCUI = genderCUI;
	}
	
    public Integer getRaceCUI() {
		return raceCUI;
	}

    public void setRaceCUI(Integer raceCUI) {
		this.raceCUI = raceCUI;
	}

    public Integer getLocationId() {
		return locationId;
	}

    public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

    public String getGenderSourceCode() {
		return genderSourceCode;
	}

    public void setEthnicityCUI(Integer ethnicityCUI) {
        this.ethnicityCUI = ethnicityCUI;
    }

    public Integer getEthnicityCUI() {
        return ethnicityCUI;
    } 

    public void setEthnicitySourceCode(String ethnicitySourceCode) {
   	    this.ethnicitySourceCode = ethnicitySourceCode;
    }

    public String getEthnicitySourceCode() {
        return ethnicitySourceCode;
    }

    public void setGenderSourceCode(String genderSourceCode) {
		this.genderSourceCode = genderSourceCode;
	}

    public String getRaceSourceCode() {
		return raceSourceCode;
	}

    public void setRaceSourceCode(String raceSourceCode) {
		this.raceSourceCode = raceSourceCode;
	}
}

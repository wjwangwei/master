package com.nehow.dao.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {
    private Long id;

    private String customerNo;

    private String customerName;

    private String customerType;

    private String businessType;

    private String customerCode;

    private String country;

    private Integer provinceId;

    private String provinceName;

    private Integer cityId;

    private String cityName;

    private String registerName;

    private String registerAddress;

    private String legalPerson;

    private String contractNo;

    private String contractStatus;

    private Date contractDate;

    private String businessLicense;

    private String logo;

    private Integer customerServiceId;

    private String customerServiceName;

    private Integer salesmanId;

    private String salesmanName;

    private BigDecimal deposit;

    private String depositCurrency;

    private Integer credit;

    private Integer creditAll;

    private String paymentType;

    private String deductBalance;

    private BigDecimal defaultMarkup;

    private String settlementType;

    private Short settlementPeriod;

    private Short settlementDate;

    private String settlementDateType;

    private String settlementCurrency;

    private Short billDate;

    private Short safeDay;

    private String status;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private String updater;

    public Customer(Long id, String customerNo, String customerName, String customerType, String businessType, String customerCode, String country, Integer provinceId, String provinceName, Integer cityId, String cityName, String registerName, String registerAddress, String legalPerson, String contractNo, String contractStatus, Date contractDate, String businessLicense, String logo, Integer customerServiceId, String customerServiceName, Integer salesmanId, String salesmanName, BigDecimal deposit, String depositCurrency, Integer credit, Integer creditAll, String paymentType, String deductBalance, BigDecimal defaultMarkup, String settlementType, Short settlementPeriod, Short settlementDate, String settlementDateType, String settlementCurrency, Short billDate, Short safeDay, String status, String remark, Date createTime, Date updateTime, String updater) {
        this.id = id;
        this.customerNo = customerNo;
        this.customerName = customerName;
        this.customerType = customerType;
        this.businessType = businessType;
        this.customerCode = customerCode;
        this.country = country;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.cityId = cityId;
        this.cityName = cityName;
        this.registerName = registerName;
        this.registerAddress = registerAddress;
        this.legalPerson = legalPerson;
        this.contractNo = contractNo;
        this.contractStatus = contractStatus;
        this.contractDate = contractDate;
        this.businessLicense = businessLicense;
        this.logo = logo;
        this.customerServiceId = customerServiceId;
        this.customerServiceName = customerServiceName;
        this.salesmanId = salesmanId;
        this.salesmanName = salesmanName;
        this.deposit = deposit;
        this.depositCurrency = depositCurrency;
        this.credit = credit;
        this.creditAll = creditAll;
        this.paymentType = paymentType;
        this.deductBalance = deductBalance;
        this.defaultMarkup = defaultMarkup;
        this.settlementType = settlementType;
        this.settlementPeriod = settlementPeriod;
        this.settlementDate = settlementDate;
        this.settlementDateType = settlementDateType;
        this.settlementCurrency = settlementCurrency;
        this.billDate = billDate;
        this.safeDay = safeDay;
        this.status = status;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.updater = updater;
    }

    public Customer() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType == null ? null : customerType.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode == null ? null : customerCode.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName == null ? null : registerName.trim();
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress == null ? null : registerAddress.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo == null ? null : contractNo.trim();
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus == null ? null : contractStatus.trim();
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense == null ? null : businessLicense.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Integer getCustomerServiceId() {
        return customerServiceId;
    }

    public void setCustomerServiceId(Integer customerServiceId) {
        this.customerServiceId = customerServiceId;
    }

    public String getCustomerServiceName() {
        return customerServiceName;
    }

    public void setCustomerServiceName(String customerServiceName) {
        this.customerServiceName = customerServiceName == null ? null : customerServiceName.trim();
    }

    public Integer getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName == null ? null : salesmanName.trim();
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public String getDepositCurrency() {
        return depositCurrency;
    }

    public void setDepositCurrency(String depositCurrency) {
        this.depositCurrency = depositCurrency == null ? null : depositCurrency.trim();
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getCreditAll() {
        return creditAll;
    }

    public void setCreditAll(Integer creditAll) {
        this.creditAll = creditAll;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    public String getDeductBalance() {
        return deductBalance;
    }

    public void setDeductBalance(String deductBalance) {
        this.deductBalance = deductBalance == null ? null : deductBalance.trim();
    }

    public BigDecimal getDefaultMarkup() {
        return defaultMarkup;
    }

    public void setDefaultMarkup(BigDecimal defaultMarkup) {
        this.defaultMarkup = defaultMarkup;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType == null ? null : settlementType.trim();
    }

    public Short getSettlementPeriod() {
        return settlementPeriod;
    }

    public void setSettlementPeriod(Short settlementPeriod) {
        this.settlementPeriod = settlementPeriod;
    }

    public Short getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Short settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getSettlementDateType() {
        return settlementDateType;
    }

    public void setSettlementDateType(String settlementDateType) {
        this.settlementDateType = settlementDateType == null ? null : settlementDateType.trim();
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency == null ? null : settlementCurrency.trim();
    }

    public Short getBillDate() {
        return billDate;
    }

    public void setBillDate(Short billDate) {
        this.billDate = billDate;
    }

    public Short getSafeDay() {
        return safeDay;
    }

    public void setSafeDay(Short safeDay) {
        this.safeDay = safeDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
    }
}
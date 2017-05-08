package com.nehow.dao;

import com.nehow.entity.CustomerSupplier;
import com.nehow.entity.CustomerSupplierExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerSupplierMapper {
    int countByExample(CustomerSupplierExample example);

    int deleteByExample(CustomerSupplierExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CustomerSupplier record);

    int insertSelective(CustomerSupplier record);

    List<CustomerSupplier> selectByExample(CustomerSupplierExample example);

    CustomerSupplier selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CustomerSupplier record, @Param("example") CustomerSupplierExample example);

    int updateByExample(@Param("record") CustomerSupplier record, @Param("example") CustomerSupplierExample example);

    int updateByPrimaryKeySelective(CustomerSupplier record);

    int updateByPrimaryKey(CustomerSupplier record);
}
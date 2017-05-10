package com.nehow.dao.mapper;

import com.nehow.dao.entity.CustomerSupplier;
import com.nehow.dao.entity.CustomerSupplierExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
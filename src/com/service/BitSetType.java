package com.service;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.BitSet;

/**
 * custom BasicType implementation,自定义类型映射
 * Name: admin
 * Date: 2017/3/20
 * Time: 16:59
 */
public class BitSetType extends AbstractSingleColumnStandardBasicType<BitSet>
                        implements DiscriminatorType<BitSet>{
    public static final BitSetType INSTANCE = new BitSetType();
    public BitSetType() {
        super(VarcharTypeDescriptor.INSTANCE,BitSetTypeDescriptor.INSTANCE);
    }

    @Override
    public BitSet stringToObject(String xml) throws Exception {
        return fromString(xml);
    }

    @Override
    public String objectToSQLString(BitSet value, Dialect dialect) throws Exception {
        return toString(value);
    }

    @Override
    public String getName() {
        return "bitset";
    }
}

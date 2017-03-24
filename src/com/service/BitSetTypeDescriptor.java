package com.service;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.util.BitSet;

/**
 * Name: admin
 * Date: 2017/3/20
 * Time: 17:07
 */
public class BitSetTypeDescriptor extends AbstractTypeDescriptor<BitSet> {
    private static final String DELIMITER = ",";

    public static final BitSetTypeDescriptor INSTANCE = new BitSetTypeDescriptor();

    protected BitSetTypeDescriptor() {
        super(BitSet.class);
    }

    @Override
    public String toString(BitSet value) {
        StringBuilder builder = new StringBuilder();
        for (long token : value.toLongArray()) {//	toLongArray():Returns a new long array containing all the bits in this bit set.
            if (builder.length() > 0) {
                builder.append(DELIMITER);
            }
            builder.append(Long.toString(token, 2));//Returns a string representation of the first argument in the radix specified by the second argument.
        }
        return builder.toString();
    }

    @Override
    public BitSet fromString(String s) {
        if ((s == null) || s.isEmpty()){
            return null;
        }
        String[] tokens = s.split(DELIMITER);//Splits this string around matches of the given regular expression.
        long[] values = new long[tokens.length];

        for (int i = 0; i< tokens.length; i++){
            values[i] = Long.valueOf(tokens[i], 2);
        }
        return BitSet.valueOf(values);
    }

    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(BitSet value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( BitSet.class.isAssignableFrom( type ) ) {
            return (X) value;
        }
        if ( String.class.isAssignableFrom( type ) ) {
            return (X) toString( value);
        }
        throw unknownUnwrap( type );
    }

    public <X> BitSet wrap(X value, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }
        if ( String.class.isInstance( value ) ) {
            return fromString( (String) value );
        }
        if ( BitSet.class.isInstance( value ) ) {
            return (BitSet) value;
        }
        throw unknownWrap( value.getClass() );
    }
}

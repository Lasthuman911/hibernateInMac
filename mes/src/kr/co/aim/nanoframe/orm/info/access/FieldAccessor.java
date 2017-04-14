package kr.co.aim.nanoframe.orm.info.access;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Name: admin
 * Date: 2017/4/13
 * Time: 9:07
 */
public class FieldAccessor {


    public FieldAccessor() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null)
                    continue;
                if (field.getType().equals(String.class))
                    field.set(this, "");
                else if (field.getType().equals(Timestamp.class))
                    field.set(this, null);
                else if (field.getType().equals(Map.class))
                    field.set(this, new HashMap());
                else if (field.getType().equals(List.class))
                    field.set(this, new ArrayList());
                else {
                    try {
                        field.set(this, Class.forName(field.getType().getName()).newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return {字段1=值1，字段2=值2}
     */
    @Override
    public String toString() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                builder.append(field.getName());
                builder.append("=");
                builder.append(field.get(this));
                builder.append(", ");
                field.setAccessible(false);
            }
            if (builder.substring(builder.length() - 2).equals(", ")) {
                builder.delete(builder.length() - 2, builder.length());
            }
            builder.append("}");
            return builder.toString();
        } catch (Exception e) {
            return super.toString();
        }
    }


}

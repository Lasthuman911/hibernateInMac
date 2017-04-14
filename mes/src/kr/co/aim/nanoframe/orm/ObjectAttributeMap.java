package kr.co.aim.nanoframe.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kr.co.aim.nanoframe.exception.ErrorSignal;
import kr.co.aim.nanoframe.util.sys.SystemPropHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ObjectAttributeMap implements ApplicationContextAware, InitializingBean, Serializable {
    private static Log log =
            LogFactory.getLog(ObjectAttributeMap.class);

    public final static String Standard_Type = "Standard";
    public final static String ExtendedC_Type = "ExtendedC";
    public final static String ExtendedT_Type = "ExtendedT";
    public static final String ObjectAttributeCondition =
            "select oad.typename, oad.position, oad.attributename, oad.primarykeyflag, oad.attributetype, oad.datatype, oad.defaultvalue,otd.extendedtflag "
                    + SystemPropHelper.CR
                    + "from objectattributedef oad, objecttypedef otd where oad.attributetype = ? and oad.typename = otd.typename order by typename, position";

    public static final String ObjectAttributeStandardWhere =
            "select typename, attributename from objectattributedef where attributetype = ? and typename = ? order by position";

    private ApplicationContext context;
    /**
     * wzm:Key : TypeName.AttributeType  ==> (ex) Lot.Standard
     */
    private Map<String, List<ObjectAttributeDef>> objectAttributeDefList =
            new ConcurrentHashMap<String, List<ObjectAttributeDef>>();

    // add : 2010.11.04  : 捞镑俊绰 AttributeTable俊 措茄 沥焊绰 啊瘤绊 乐瘤 臼澜. (LotAttribute, LotAttributeHistory 殿篮 狐廉乐澜)
    // <TypeName.AttributeType, <attributeName, ObjectAttributeDef>>
    private Map<String, Map<String, ObjectAttributeDef>> objectAttributeDefMap =
            new ConcurrentHashMap<String, Map<String, ObjectAttributeDef>>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    /**
     * wzm:
     *
     * @return
     */
    public Map<String, List<ObjectAttributeDef>> getMap() {
        return objectAttributeDefList;
    }

    /**
     * wzm:凡是实现了InitializingBean接口，在初始化bean的时候会执行此方法
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        load();
    }

    /**
     * wzm: afterPropertiesSet 中只有load此方法
     */
    public synchronized void load() {
        try {

            Map<String, List<ObjectAttributeDef>> attributeDefNewList =
                    new ConcurrentHashMap<String, List<ObjectAttributeDef>>();
            Map<String, Map<String, ObjectAttributeDef>> attributeDefNewMap =
                    new ConcurrentHashMap<String, Map<String, ObjectAttributeDef>>();

            // this.getMap().clear();
            String[] attributeTypes = new String[]{Standard_Type, ExtendedC_Type, ExtendedT_Type};

            for (String attributeType : attributeTypes) {
                List<Map<String, Object>> resultList =
                        ((SqlTemplate) context.getBean(SqlTemplate.class.getSimpleName())).queryForList(
                                ObjectAttributeCondition, new Object[]{attributeType});

                String currentTypeName = "";
                String currentExtendedTFlag = "";

                for (Map<String, Object> map : resultList) {
                    String typeName = (String) map.get("typename");
                    int position = Integer.parseInt(map.get("position").toString());
                    String attributeName = (String) map.get("attributename");
                    String primaryKeyFlag = (String) map.get("primarykeyflag");
                    String attributetype = (String) map.get("attributetype");
                    String datatype = (String) map.get("datatype");
                    String defaultValue = (String) map.get("defaultvalue");
                    String extendedtflag = (String) map.get("extendedtflag");

                    ObjectAttributeDef ObjectAttributeDef = new ObjectAttributeDef();
                    ObjectAttributeDef.setTypeName(typeName);
                    ObjectAttributeDef.setPosition(position);
                    ObjectAttributeDef.setAttributeName(attributeName);
                    ObjectAttributeDef.setAttributeType(attributetype);
                    ObjectAttributeDef.setDataType(datatype);
                    ObjectAttributeDef.setPrimaryKeyFlag(primaryKeyFlag);
                    ObjectAttributeDef.setDefaultValue(defaultValue);

                    String key = typeName + "." + attributeType;
                    List<ObjectAttributeDef> ObjectAttributelist = attributeDefNewList.get(key);
                    if (ObjectAttributelist == null) {
                        createAttributeTables(currentTypeName, currentExtendedTFlag, attributeDefNewList);
                        createAttributeHistoryTables(currentTypeName, currentExtendedTFlag, attributeDefNewList);

                        ObjectAttributelist = new ArrayList<ObjectAttributeDef>();
                        attributeDefNewList.put(key, ObjectAttributelist);
                    }
                    ObjectAttributelist.add(ObjectAttributeDef);

                    Map<String, ObjectAttributeDef> ObjectAttributeMap = attributeDefNewMap.get(key);
                    if (ObjectAttributeMap == null) {
                        ObjectAttributeMap = new HashMap<String, ObjectAttributeDef>();
                        ObjectAttributeMap.put(key, ObjectAttributeDef);
                    }
                    ObjectAttributeMap.put(ObjectAttributeDef.getAttributeName(), ObjectAttributeDef);

                    currentTypeName = typeName;
                    currentExtendedTFlag = extendedtflag;
                }
            }

            this.objectAttributeDefList = attributeDefNewList;
            this.objectAttributeDefMap = attributeDefNewMap;

            log.info("Completed to load ObjectAttributeMap");
        } catch (Throwable e) {
            ErrorSignal.notifyException(e);
        }
    }

    /**
     * wzm:
     *
     * @param typeName
     * @param extendedtflag
     * @param attributeDefMap
     */
    private void createAttributeTables(String typeName, String extendedtflag,
                                       Map<String, List<ObjectAttributeDef>> attributeDefMap) {
        if (!extendedtflag.equalsIgnoreCase("y") || StringUtils.isEmpty(typeName))
            return;
        List<ObjectAttributeDef> attributeNames = attributeDefMap.get(typeName + "Attribute" + "." + Standard_Type);

        if (attributeNames == null) {
            attributeNames = new ArrayList<ObjectAttributeDef>();
            attributeDefMap.put(typeName + "Attribute" + "." + Standard_Type, attributeNames);

            List<ObjectAttributeDef> attributeNamesNotAttribute = attributeDefMap.get(typeName + "." + Standard_Type);
            for (int i = 0; i < attributeNamesNotAttribute.size(); i++) {
                if (attributeNamesNotAttribute.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y")) {
                    ObjectAttributeDef ObjectAttributeDef = new ObjectAttributeDef();
                    ObjectAttributeDef.setAttributeName(attributeNamesNotAttribute.get(i).getAttributeName());
                    ObjectAttributeDef.setAttributeType(attributeNamesNotAttribute.get(i).getAttributeType());
                    ObjectAttributeDef.setDataType(attributeNamesNotAttribute.get(i).getDataType());
                    ObjectAttributeDef.setPosition(attributeNamesNotAttribute.get(i).getPosition());
                    ObjectAttributeDef.setPrimaryKeyFlag(attributeNamesNotAttribute.get(i).getPrimaryKeyFlag());
                    ObjectAttributeDef.setTypeName(attributeNamesNotAttribute.get(i).getTypeName());
                    ObjectAttributeDef.setDefaultValue(attributeNamesNotAttribute.get(i).getDefaultValue());
                    attributeNames.add(ObjectAttributeDef);
                } else {
                    String[] fieldNames = new String[]{"attributeName", "attributeValue"};
                    String[] dataTypes = new String[]{"String", "String"};
                    String[] primaryKeys = new String[]{"Y", "N"};

                    for (int j = 0; j < fieldNames.length; j++) {
                        ObjectAttributeDef ObjectAttributeDef = new ObjectAttributeDef();
                        ObjectAttributeDef.setAttributeName(fieldNames[j]);
                        ObjectAttributeDef.setAttributeType(Standard_Type);
                        ObjectAttributeDef.setDataType(dataTypes[j]);
                        ObjectAttributeDef.setPosition(attributeNames.size());
                        ObjectAttributeDef.setPrimaryKeyFlag(primaryKeys[j]);
                        ObjectAttributeDef.setTypeName(typeName + "Attribute");
                        attributeNames.add(ObjectAttributeDef);
                    }
                    break;
                }
            }

        }
    }

    private void createAttributeHistoryTables(String typeName, String extendedtflag,
                                              Map<String, List<ObjectAttributeDef>> attributeDefMap) {
        if (!extendedtflag.equalsIgnoreCase("y") || StringUtils.isEmpty(typeName))
            return;

        List<ObjectAttributeDef> attributeNames = getAttributeNames(typeName + "AttributeHistory", Standard_Type);

        if (attributeNames == null) {
            attributeNames = new ArrayList<ObjectAttributeDef>();
            attributeDefMap.put(typeName + "AttributeHistory" + "." + Standard_Type, attributeNames);

            List<ObjectAttributeDef> attributeNamesNotAttribute = attributeDefMap.get(typeName + "." + Standard_Type);
            for (int i = 0; i < attributeNamesNotAttribute.size(); i++) {
                if (attributeNamesNotAttribute.get(i).getPrimaryKeyFlag().equalsIgnoreCase("y")) {
                    ObjectAttributeDef ObjectAttributeDef = new ObjectAttributeDef();
                    ObjectAttributeDef.setAttributeName(attributeNamesNotAttribute.get(i).getAttributeName());
                    ObjectAttributeDef.setAttributeType(attributeNamesNotAttribute.get(i).getAttributeType());
                    ObjectAttributeDef.setDataType(attributeNamesNotAttribute.get(i).getDataType());
                    ObjectAttributeDef.setPosition(attributeNamesNotAttribute.get(i).getPosition());
                    ObjectAttributeDef.setPrimaryKeyFlag(attributeNamesNotAttribute.get(i).getPrimaryKeyFlag());
                    ObjectAttributeDef.setTypeName(attributeNamesNotAttribute.get(i).getTypeName());
                    ObjectAttributeDef.setDefaultValue(attributeNamesNotAttribute.get(i).getDefaultValue());
                    attributeNames.add(ObjectAttributeDef);
                } else {
                    String[] fieldNames =
                            new String[]{
                                    "attributeName",
                                    "timeKey",
                                    "eventTime",
                                    "eventName",
                                    "oldAttributeValue",
                                    "attributeValue"};
                    String[] dataTypes = new String[]{"String", "String", "timeStamp", "String", "String", "String"};
                    String[] primaryKeys = new String[]{"Y", "Y", "N", "N", "N", "N"};

                    for (int j = 0; j < fieldNames.length; j++) {
                        ObjectAttributeDef ObjectAttributeDef = new ObjectAttributeDef();
                        ObjectAttributeDef.setAttributeName(fieldNames[j]);
                        ObjectAttributeDef.setAttributeType(Standard_Type);
                        ObjectAttributeDef.setDataType(dataTypes[j]);
                        ObjectAttributeDef.setPosition(attributeNames.size());
                        ObjectAttributeDef.setPrimaryKeyFlag(primaryKeys[j]);
                        ObjectAttributeDef.setTypeName(typeName + "AttributeHistory");
                        attributeNames.add(ObjectAttributeDef);
                    }
                    break;
                }
            }

        }
    }

    /**
     * wzm：
     *
     * @param tableName
     * @param attributeTypeName
     * @return
     */
    public List<ObjectAttributeDef> getAttributeNames(String tableName, String attributeTypeName) {
        return this.getMap().get(tableName + "." + attributeTypeName);
    }

    // add : 2010.11.04
    public Map<String, ObjectAttributeDef> getAttributeNamesByMap(String typeName, String attributeTypeName) {
        Map<String, ObjectAttributeDef> result = this.objectAttributeDefMap.get(typeName + "." + attributeTypeName);
        if (result != null)
            return result;

        // objectAttributeDefMap俊绰  AttributeTable俊 措茄 沥焊绰 啊瘤绊 乐瘤 臼扁 锭巩烙. (LotAttribute, LotAttributeHistory 殿篮 狐廉乐澜)
        List<ObjectAttributeDef> defList = getAttributeNames(typeName, attributeTypeName);
        if (defList != null) {
            result = new HashMap<String, ObjectAttributeDef>();
            for (ObjectAttributeDef objectAttributeDef : defList) {
                result.put(objectAttributeDef.getAttributeName(), objectAttributeDef);
            }
        }

        return result;
    }
}

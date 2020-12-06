package ooo.poorld.mycard.common;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * author: teenyda
 * date: 2020/11/21
 * description:
 */
public class DateTypeConverter implements PropertyConverter<DataType, Integer> {
    /*@Override
    public DataType convertToEntityProperty(String databaseValue) {
        return DataType.valueOf(databaseValue);
    }*/

    @Override
    public DataType convertToEntityProperty(Integer databaseValue) {
        return DataType.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(DataType entityProperty) {
        return entityProperty.getType();
    }
}

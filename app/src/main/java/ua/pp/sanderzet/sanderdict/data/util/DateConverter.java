package ua.pp.sanderzet.sanderdict.data.util;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by sander on 27/08/17.
 */

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp (Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
        @TypeConverter
        public static Long toTimestamp (Date date){
        return date == null ? null : date.getTime();
    }
}

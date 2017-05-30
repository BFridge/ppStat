package fridge.com.hilistview.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fridge.com.hilistview.R;

public class StringUtils {

    private static long MINUTER = 60 * 1000;
    private static long HOUR = 60 * MINUTER;
    private static long DAY = 24 * HOUR;

    /**
     * 泡泡统一数字展示逻辑
     * @param count
     * @return
     */
    //    public static String getUnifiedDisplayCount(long count) {
    //        String str = "";
    //        DecimalFormat df = new DecimalFormat("#");
    //
    //        df.setRoundingMode(RoundingMode.FLOOR);//不需要四舍五入
    //
    //        if (count < 1E5) {
    //            str += count;
    //        } else if (1E5 <= count && count < 1E8) {
    //            str += df.formatMonth(count / 1E4) + "万";
    //        } else {
    //            str += df.formatMonth(count / 1E8) + "亿";
    //        }
    //        return str;
    //    }

    /**
     * 数字统一展示逻辑
     * 1、  数量超过1万显示xx万，超过亿显示xx亿
     * 2、  有小数时显示一位小数
     * 3、  对数量进行四舍五入
     */
    private static DecimalFormat unifiedDecimalFormat;

    public static String getUnifiedDisplayCount(long count) {
        String str = "";
        if(null == unifiedDecimalFormat){
            //优化Feed流中每条Feed多次创建DecimalFormat对象造成的时间消耗
            unifiedDecimalFormat = new DecimalFormat("#.#");
        }
        if (count < 10E7 && count >= 10E3) {
            str += unifiedDecimalFormat.format(count / 10E3) + "万";
        } else if (count >= 10E7) {
            str += unifiedDecimalFormat.format(count / 10E7) + "亿";
        } else {
            str += count;
        }
        return str;
    }


    public static int getTimeState(long timestamp) {
        Calendar currentTime = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
        today.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天
        yesterday.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        currentTime.setTimeInMillis(timestamp * 1000);

        //今天
        if (currentTime.after(today)) {
            return 0;
        }//昨天
        else if (currentTime.before(today) && currentTime.after(yesterday)) {
            return 1;
        } else {
            return -1;
        }
    }

    public static boolean isToday(long timestamp) {

        Calendar currentTime = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
        today.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数
        // Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        currentTime.setTimeInMillis(timestamp * 1000);

        return currentTime.after(today);
    }

    public static String getNoNullString(String text) {
        return (text == null ? "" : text);
    }

    /**
     * 基线综艺类视频期数显示规则，今年只放月-日，之前的放年-月-日
     *
     * @param dateString 格式：yyyy-mm-dd
     */
    public static String getShowDate(String dateString) {
        String dateNew = "";
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
        try {
            Date date = dateFormat1.parse(dateString);
            //如果是当年，则不显示年
            if (date.getYear() == new Date().getYear()) {
                dateNew = dateFormat2.format(date);
            } else {
                dateNew = dateString;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateNew;
    }

    /**
     * 基线资讯类视频发布时间显示规则，昨日之前的显示年-月-日，昨日今日显示时-分
     *
     * @param dateString 格式：yyyy-MM-dd
     * @param timeString 格式：HH:mm:ss
     */
    public static String getInformationTime(String dateString, String timeString) {
        //没有日期返回空
        if (TextUtils.isEmpty(dateString)) {
            return "";
        }
        //有日期没有时间返回日期
        if (TextUtils.isEmpty(timeString)) {
            return dateString;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(dateString + " " + timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //过滤掉不合法格式导致的date为null的情况
        if (date == null) {
            return "";
        }

        Calendar currentTime = Calendar.getInstance();
        Calendar today = Calendar.getInstance();    //今天
        today.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
        today.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数
        // Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天
        yesterday.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, currentTime.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, currentTime.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);

        currentTime.setTime(date);

        //今天
        if (currentTime.after(today)) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return "今日" + timeFormat.format(date);
        }
        //昨天
        else if (currentTime.before(today) && currentTime.after(yesterday)) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            return "昨日" + timeFormat.format(date);
        } else {
            return dateString;
        }
    }


    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static double getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return Math.ceil(valueLength);
    }

    /**
     * 得到一个subStringLeng长度的字符串．长度计算方式为,一个汉字或日韩文长度为1,英文字符长度为0.5
     *
     * @param s 需要被截断的字符串
     * @param subStringLength 　需要的字符串长度
     * @return String 得到的字符串
     */
    public static String getSubString(String s, int subStringLength) {
        double valueLength = 0;
        int result = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
            if (valueLength > subStringLength) {
                break;
            } else {
                result++;
            }
        }
        return s.substring(0, result);
    }

    /**
     * V7.5.1 新增  yyyy-MM-dd转换为年-月-日，MM-dd转换为月-日
     *
     * @param date 需要转换单位毫秒的日期
     */
    public static String getShowChineseDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        StringBuilder dateNew = new StringBuilder();
        dateNew.append(calendar.get(Calendar.MONTH) + 1)
                .append("月")
                .append(calendar.get(calendar.DAY_OF_MONTH))
                .append("日");
        return dateNew.toString();
    }

    /**
     * 格式化时间展示为05’10”
     */
    public static String formatRecordTime(long recTime, long maxRecordTime) {
        int time = (int) ((maxRecordTime - recTime) / 1000);
        int minute = time / 60;
        int second = time % 60;
        return String.format("%2d’%2d”", minute, second);
    }

    public static String formatTime(int recTime) {
        int minute = recTime / 60;
        int second = recTime % 60;
        return String.format("%2d’%2d”", minute, second);
    }

    public static String printFullTimeFromSecond(long value) {
        Date data = new Date(value * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(data);
        return dateString;
    }

    public static String getAvatarDecorateValidDateTime(long value) {
        Date data = new Date(value * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String dateString = formatter.format(data);
        return dateString;
    }
    /**
     * V7.8 add 计算指定月份的天数
     *
     * @param year 年份
     * @param month 月份
     * @return 总天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);//本月第一天减1，表示回滚到本月最后1天的日期。
        return calendar.get(Calendar.DATE);
    }

    /**
     * V7.8 add 计算指定日期前month个月的日期
     */
    public static Date getDateBeforeMonth(int month, Date nowDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.add(Calendar.MONTH, -month);
        return calendar.getTime();
    }

    /**
     * 格式化时间 单位秒
     *
     * @return x.x小时 或者 x分钟
     */
    public static String getDurationFormat(int duration) {
        int hour = duration / 3600;
        int minuter = duration / 60 - hour * 60;
        int second = duration - 60 * minuter - hour * 3600;
        StringBuilder strBuild = new StringBuilder();
        if (hour > 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            strBuild.append(decimalFormat.format(duration * 1.0 / 3600) + "小时");
        } else {
            strBuild.append(minuter + "分钟");
        }
        return strBuild.toString();
    }

    /**
     *
     * @param millis 获取指定毫秒数的对应星期
     * @return 星期几
     */
    public static String getWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String week = "";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }
        return "星期"+week;
    }

    public static int getCurMonth(long duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(duration * 1000);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurDayOfMonth(long duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(duration * 1000);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断两个日期是否是同一天
     */
    public static boolean isSameDate(long date1, long date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate =
                isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    /**
     * V7.10 add 显示特殊数字颜色
     * V7.11 modify 根据正则规则处理数字特殊颜色
     * V8.2 modify 添加一个结束end限制
     * @param context
     * @param regex 要匹配的正则表达式
     * @param spannableString
     * @param color  要匹配的颜色
     * @param start 最开始一个字符串下标值
     * @param end 最后一个字符串下标值
     * @return
     */
    public static CharSequence processColorByRegex(final Context context, String regex,
            SpannableString spannableString, int color, int start, int end) {
        if(context == null ||  TextUtils.isEmpty(spannableString) || start >= end){
            return spannableString;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            final String text = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            if(matcher.start() >= end){
                break;
            }
            if (!TextUtils.isEmpty(text)) {
                int mStart = matcher.start();
                int mEnd = mStart + text.length();
                ForegroundColorSpan textColor =
                        new ForegroundColorSpan(context.getResources().getColor(color));
                spannableString.setSpan(textColor, mStart, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    public static CharSequence processTextSize(String weekPeriodDoc, int text_size_in_dp, int start,
            int end, boolean isBold) {
        if (TextUtils.isEmpty(weekPeriodDoc)
                || start >= end
                || start < 0
                || end > weekPeriodDoc.length()) {
            return weekPeriodDoc;
        }
        //AbsoluteSizeSpan(int size, boolean isdip)
        AbsoluteSizeSpan textSizeSpan = new AbsoluteSizeSpan(text_size_in_dp, true);
        SpannableString spanString = new SpannableString(weekPeriodDoc);
        spanString.setSpan(textSizeSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isBold) {
            spanString.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        CharSequence rtn = spanString;
        return rtn;
    }

    /**
     * V7.11 add 处理文本的颜色
     *
     * @return CharSequence引用代表spannableString
     */
    public static CharSequence processTextColor(Context context, SpannableString spannableString,
            int color, int start, int end) {
        if (TextUtils.isEmpty(spannableString) || start >= end || start < 0 || end > spannableString
                .length()) {
            return spannableString;
        }
        ForegroundColorSpan textColor =
                new ForegroundColorSpan(context.getResources().getColor(color));
        spannableString.setSpan(textColor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * 保留两位非0小数 0.111->0.1  3.0->3
     * @param num
     * @return
     */
    public static String formatNumWith2Decimal(float num) {
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(num);
        if (!TextUtils.isEmpty(result) && result.endsWith(".0")) {
            result = result.substring(0, result.indexOf("."));
        }
        return result;
    }

    /**
     * 应援金额转换
     * @param num 待转换金额,单位为分
     */
    public static String formatRMB2String(long num){
        double numOfYuan = num * 1.0 / 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String result = df.format(numOfYuan);
        return result;
    }

    /**
     * 将 yyyymm 转化为 yyyy年m月
     */
    public static String formatMonth(String dateString) {
        String dateNew = dateString;
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMM", Locale.CHINESE);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年M月", Locale.CHINESE);
        try {
            Date date = dateFormat1.parse(dateString);
            dateNew = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateNew;
    }

    public static CharSequence processColorByRegex(Context context, String regex, SpannableString spannableString, int txt_color) {
        return processColorByRegex(context,regex,spannableString,txt_color,0,spannableString.length()-1);
    }
}

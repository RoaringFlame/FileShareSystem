package com.fss.util;

import com.fss.dao.enums.Gender;
import com.fss.dao.enums.UserRole;

public class HeadPictureUtil {
    public static Integer getPictureId(Gender gender, UserRole userRole) {
        return getPictureId(gender.ordinal(), userRole.ordinal());
    }

    public static String getPictureName(Gender gender, UserRole userRole) {
        return getPictureName(gender.ordinal(), userRole.ordinal());
    }

    public static Integer getPictureId(int genderId, int roleId) {
        int pictureId = 0;
        if (genderId == 1) {
            pictureId += 3 - roleId;
        } else {
            pictureId += 6 - roleId;
        }
        return pictureId;
    }

    public static String getPictureName(int genderId, int userRoleId) {
        return "av" + getPictureId(genderId, userRoleId) + ".png";
    }
}

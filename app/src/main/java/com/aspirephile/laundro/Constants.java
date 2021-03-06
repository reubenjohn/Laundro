package com.aspirephile.laundro;

public class Constants {
    public static final class files {

        public static final String firstRun = "firstRun";
        public static String authentication = "authentication";
        public static String settings = "settings";
    }

    public static final class properties {

        public static final long splashScreenDuration = 1500;
    }

    public static final class errorResults {
        public static final String pointNotFound = "POINT_NOT_FOUND";
        public static final String badPID = "BAD_PID";
        public static final String badIntent = "BAD_INTENT";
        public static final String badUsername = "BAD_USERNAME";
    }


    public static class extras {

        public static final String _id = "_id";
        public static final String errorResult = "errorResult";
        public static final String CID = "CID";
    }

    public static final class preferences {

        public static final String username = "email";
        public static final String url = "url";
        public static final String userId = "userId";

        public static final class pointCreator {

            public static final int titleMin = 15;
            public static final int titleMax = 60;
            public static final int descriptionMin = 20;
            public static final int descriptionMax = 4000;
        }
    }

    public static final class codes {

        public static final class request {

            public static final int authentication = 1111;
            public static final int about = 1121;
        }

        public static final class result {

            public static final int point_viewer = 1131;
            public static final int point_creator = 1141;
        }
    }

    public static class tags {

        public static final String pointListFragment = "pointListFragment";
        public static final String pointViewerFragment = "pointViewerFragment";
        public static final String splashFragment = "splashFragment";
        public static final String commentListFragment = "commentListFragment";
        public static final String billViewerFragment = "billViewerFragment";
    }
}

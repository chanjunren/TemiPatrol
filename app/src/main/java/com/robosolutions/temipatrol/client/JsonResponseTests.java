package com.robosolutions.temipatrol.client;

public class JsonResponseTests {
    public static final String MASK_RESPONSE_TEST = "{\n" +
            "  \"FACE_MASK_DETECTION\": [\n" +
            "    {\n" +
            "      \"boundingPoly\": {\n" +
            "        \"normalizedVertices\": [\n" +
            "          {\n" +
            "            \"x\": 0.46406767,\n" +
            "            \"y\": 0.47452173,\n" +
            "            \"width\": 0.32452173,\n" +
            "            \"height\": 0.27452173,\n" +
            "            \"score\": 0.98216777\n" +
            "          },\n" +
            "          {\n" +
            "            \"x\": 0.8672081,\n" +
            "            \"y\": 0.57452173,\n" +
            "            \"width\": 0.27752173,\n" +
            "            \"height\": 0.27452173,\n" +
            "            \"score\": 0.99772167\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"name\": \"Face Mask\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"boundingPoly\": {\n" +
            "        \"normalizedVertices\": [\n" +
            "          {\n" +
            "            \"x\": 0.66406767,\n" +
            "            \"y\": 0.57452173,\n" +
            "            \"width\": 0.30452173,\n" +
            "            \"height\": 0.26452173,\n" +
            "            \"score\": 0.95216777\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"name\": \"Face Mask\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    public static final String HD_RESPONSE_TEST_1 = "{\n" +
            "    \"HUMAN_DETECTION\": {\n" +
            "        \"boundingPoly\": {\n" +
            "            \"normalizedVertices\": [\n" +
            "                {\n" +
            "                    \"id\": 0,\n" +
            "                    \"x0\": 330.0,\n" +
            "                    \"y0\": 169.0,\n" +
            "                    \"x1\": 210.0,\n" +
            "                    \"y1\": 412.0,\n" +
            "                    \"tilt\": 0.896630\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 1,\n" +
            "                    \"x0\": 639.0,\n" +
            "                    \"y0\": 147.0,\n" +
            "                    \"x1\": 762.0,\n" +
            "                    \"y1\": 466.0,\n" +
            "                    \"tilt\": 0.93304\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"name\": \"Human\"\n" +
            "    },\n" +
            "    \"HUMAN_DISTANCE\": [\n" +
            "        {\n" +
            "            \"distanceMatrix\": [\n" +
            "                [\n" +
            "                    0.0,\n" +
            "                    98.82162475585938\n" +
            "                ],\n" +
            "                [\n" +
            "                    98.82162475585938,\n" +
            "                    0.0\n" +
            "                ]\n" +
            "            ],\n" +
            "            \"human_num\": 2\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    public static final String HD_RESPONSE_TEST_2 = "{\n" +
            "    \"HUMAN_DETECTION\": {\n" +
            "        \"boundingPoly\": {\n" +
            "            \"normalizedVertices\": [\n" +
            "                {\n" +
            "                    \"id\": 0,\n" +
            "                    \"x0\": 330.0,\n" +
            "                    \"y0\": 169.0,\n" +
            "                    \"x1\": 210.0,\n" +
            "                    \"y1\": 412.0,\n" +
            "                    \"tilt\": 0.896630\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 1,\n" +
            "                    \"x0\": 639.0,\n" +
            "                    \"y0\": 147.0,\n" +
            "                    \"x1\": 762.0,\n" +
            "                    \"y1\": 466.0,\n" +
            "                    \"tilt\": 0.93304\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"name\": \"Human\"\n" +
            "    },\n" +
            "    \"HUMAN_DISTANCE\": [\n" +
            "        {\n" +
            "            \"distanceMatrix\": [\n" +
            "                [\n" +
            "                    0.0,\n" +
            "                    98.82162475585938\n" +
            "                ],\n" +
            "                [\n" +
            "                    98.82162475585938,\n" +
            "                    0.0\n" +
            "                ]\n" +
            "            ],\n" +
            "            \"human_num\": 2\n" +
            "        }, \n" +
            "        {\n" +
            "            \"distanceMatrix\": [\n" +
            "                [\n" +
            "                    0.0,\n" +
            "                    98.82162475585938\n" +
            "                ],\n" +
            "                [\n" +
            "                    98.82162475585938,\n" +
            "                    0.0\n" +
            "                ]\n" +
            "            ],\n" +
            "            \"human_num\": 8\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";

    public static final String HD_RESPONSE_TEST_3 = "{\n" +
            "    \"HUMAN_DETECTION\": {\n" +
            "        \"boundingPoly\": {\n" +
            "            \"normalizedVertices\": [\n" +
            "                {\n" +
            "                    \"id\": 0,\n" +
            "                    \"x0\": 330.0,\n" +
            "                    \"y0\": 169.0,\n" +
            "                    \"x1\": 210.0,\n" +
            "                    \"y1\": 412.0,\n" +
            "                    \"tilt\": 0.896630\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 1,\n" +
            "                    \"x0\": 639.0,\n" +
            "                    \"y0\": 147.0,\n" +
            "                    \"x1\": 762.0,\n" +
            "                    \"y1\": 466.0,\n" +
            "                    \"tilt\": 0.93304\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        \"name\": \"Human\"\n" +
            "    },\n" +
            "    \"HUMAN_DISTANCE\": [\n" +
            "        {\n" +
            "            \"distanceMatrix\": [\n" +
            "                [\n" +
            "                    0.0,\n" +
            "                    98.82162475585938\n" +
            "                ],\n" +
            "                [\n" +
            "                    98.82162475585938,\n" +
            "                    0.0\n" +
            "                ]\n" +
            "            ],\n" +
            "            \"human_num\": 20\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";
}

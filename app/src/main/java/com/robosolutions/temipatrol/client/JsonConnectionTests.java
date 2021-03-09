package com.robosolutions.temipatrol.client;

public class JsonConnectionTests {
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

    public static final String HUMAN_DETECTION_REQ = "{\n" +
            "  \"requests\": [\n" +
            "    {\n" +
            "      \"features\": [\n" +
            "        {\n" +
            "         \"min_height\": 0.03,\n" +
            "         \"min_width\": 0.03,\n" +
            "         \"maxResults\": 20,\n" +
            "         \"score_th\": 0.30,\n" +
            "         \"nms_iou\": 0.40,\n" +
            "         \"type\": \"HUMAN_DETECTION\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"image\": {\n" +
            "        \"content\": \"data:image/png;base64,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\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    public static final String HUMAN_DIST_REQ = "{\n" +
            "  \"requests\": [\n" +
            "    {\n" +
            "      \"features\": [\n" +
            "        {\n" +
            "          \"Fx\": 823,\n" +
            "          \"Fy\": 825,\n" +
            "          \"centerX\": 323,\n" +
            "          \"type\": \"HUMAN_DISTANCE\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"image\": {\n" +
            "        \"content\": \"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\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    public static final String MASK_DETECTION_REQ = "{\n" +
            "  \"requests\": [\n" +
            "    {\n" +
            "      \"features\": [\n" +
            "        {\n" +
            "          \"maxResults\": 20,\n" +
            "          \"type\": \"FACEMASK_DETECTION\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"image\": {\n" +
            "        \"content\": \"iVBORw0KGgoAAAANSUhEUgAAAHgAAAB4CAMAAAAOusbgAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAACjlBMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHCAnC2/zC3f0AAAC81vm92/9IUl8AAAAAAAATGiA4Rlo7S19BVG1Zc5Jykbd3msMkLzsAAAAAAAAhKh17nW2z5Z+Gq3eRuoELDwqn1pU4SDJDVjwtOiglKi9LVF54cHqglqOflaKUipeJgY2JgIxmYGlGUF0jKC6vxN3J4P0MDg8oJimckp6MhJAiICMLDQ+81vmYrco6Qk0ZHB+jts1kcH5aVFszMDVea3yNoLoXGh8+Rk+80u1QS1KWjJiVi5c9Oj+wyOmku9kKCQpGQkdaVFx4cXqWqL0yOD+mvNlXYm7F3vy91/nC2vvH3v1SXWxMVF8ZHCAYGyBHUV59jJ51hZsNDhBmaW+gpq+vtb+nr7ukrrqWn6tfZWwMDhBxfo7q8v/f6vrb6PlpeIxJS09ESE0vNT51eX9tdHy+xM+wtsDb4u/M098mMT5NYnw6SV2TmKAeHyA7PUAdJC6bxPl0k7pYW2APEBCSl599g4wJDA9gepuRt+kODxDN2el9n8qIkZuHq9mDiI97gownMT8KDRAdHh8dJS+/y9lXWl9TV10rLS/R2+oODw9SV11OYn06Sl6hp7CXoKwcHSCPorq/2Pm+1/p2hpxea31lcH9xfo/I4P3G3vwqFxg4HyEOBwjB2vqoX2Pgf4TSd3uKmq6XqL691vlGJymMT1LE3Px+R0qKmq22Z2scEBHA2frD3PvE3fyBk6szOEDE2/t+jJ8xNz8vNj8oJSgeHB+Tpb2/2PpqhqtDVWwTGB89RE86PD////8lmllPAAAAK3RSTlMAf++fr4AQQGAgoD8w0L/fsFDgkPCPX89v+qpxcP4j5sBObpaclIFyb34GSZXAwwAAAAFiS0dE2XcKdzgAAAAHdElNRQflAwQUMiyGGP4oAAAAAW9yTlQBz6J3mgAAB7JJREFUaN7tmvtbFFUYx/fizsICC+xuQEW13S/HSKkgLcNdylwEq0W6ABpEF8SAME1RS4QFTFmUYNUkRMksuxqaZkZl2ZUuWmr+OZ13zszs7O6ZnTl7ex6L7w8MOzPnfOY9533PXaeLkN6AEDLodamWERHNSjHXhESZUgvG5Tz7zqK7ZuPS5lLJNWNT5xQVFc3BV3MqwWkYWASam+KyBouLMbc41RanY+DdxUXF9+BreirBOgsmzr0XFzSyRHxUhpoy4wBzWUI0ZcmcOkNvNSBtyrbqM2IkW/gMciSu2agVKspgjM09cnHaXAlrZaQSWWNBm4Mezc0i+dgsJrM2mSw2ocll980gOJcvZJudLY90u4Mv8FymVHKwHtI70mIotDQefUWMYL6fsotOxqkGkzyeODskNsYEBq5DKK48Y77WcDLmkU/NdTCTCRj6RxtvAadniyeDnkdnAtnODIaYsvEZ5LGGMUYX8B/skMelRjD4swPs5Ugcl5TiP/fNU9D8+x9YgPUgfqdsIQkm0WaWXh3A2cLHcnzdlrrc7nKEHpqnqIcXYfAjCC32eCqWIDEschHTEMpMyssuciur3FhL8X+PKpPnL1rwGH7jcQ+WtxpdSQy1I5belYAdkBQarmU1wHXXYJPRE08qkp96Gj+vrvXw8tZdpROr2coIhnYDxiMlhOt2V8Hd+uUrnqFpxfJ6/HRxhUfS1Xxehfiu5s6SBzvge7GHVYpcTC5XcWY513PNtaLJFiYw1DCEcoM7KFdjVG7jsx65rstOI7VsYAJnEoNL3SFqaCpRoJY0Vbmfk3O9hJjJ4F4AtulIMLjcLHpeBq4TAtKmfbgKYKgXPBQpYeK6X5CbXC3l4mQAw0fiRmQpG/hFObgZN0PEUfIZwFAtYa6lRWGVjIQKYwNzEti1soWhh1j1Up0cbGYGw4XntjL3Tm0Arg1mExuYnSuQ4wTXtOPryx2rNemVNWvWvorfX1cbP3g9vqzu1KgNG7HW4hSb4gfjZvI1rVwC3vg6QptjAOdBX5hvteZL4C5G8BYJLGRjzFPHpocMJ+MFS8pXnROEDmPDwd09PT4qsbenpzsaWHU4AF1/Xz+vrRHgN7ZBFtu7I7Dd2+HBto4I8FaSVR8iA4sowt3ngJ9oMBy8Q/j4nUNh3CHRrB3h4EEhrwHVEbYToX4FcJdUbDtDbe4OFuibCuB+1S5KGezbidDwiD+A8wiLLyjn/oB/ZBihXbsTD4by3CNkguQe5kNCGkjwVuLBXWLt7w1rylbj33vFmtySFHAffz9AAQf4J31JAY+K+Y/gf3rDfGtE/KK3Ew/uxT/HMHlwH0LjIc41jtA+TAjsx28cSDy4cwLiZQxyRx0h4A64tX8M/r6ThHDq9B0Uo3UirAGZEB8cfDcZ4E4fARwajWgyRw+RD/JtSAoY1/NoV9cQrZfwDXV1jfZGdhKJAqvqMgTnYP+MAE8wgt+LAO9XBUN/PPY+r8ME3IR7o14m8IFdCDUT8GGSFYSaClgnLldLIxAXdPEf+DSDd3/4EU5RFz4CUQVn2sLAYDKzmj3MYB1X6CTKEsDuUmZumUcAZwl5ObXPVWVzJ3drJRO2us3jCU5heJliA7vdVQ3a9LHX660T56mJAGvVJ/IJ8mUHhvWaKkbwp3hafOQzQbGCFZciXJOha0Guo9Jvj6dtndzPxIVyOyu4lcLFE+aWkBstqF1Y/jvm2RTm4cLMJZ9hfY/vMJZRwA0odPnLFSyZz+sAdvyEoC+QMHPRI9UpjFyFCLXTLEahy1BLg054Egz+8pSor44jZLBarbD4bdPO5XdW11PIsKK5cuproqmVKFgw3yxB6NtTQZ2WitzBtN+Jy7q9JhIcsYpbLjj/d54yhE7IwN+Lb2Sx7bNCJDdRTD4TSi4/I/k0FPVpifvDjxhpwmLeaMxRcGxXU7D1rmwSPe0nHMSLwxvu2PZV0x0KZJp+htaqLYzLEEQhgtYLTWri/kLayV+rZVhH7OcbYCiEpjWspf4mNdEVZTzUbio0x3OShN8iRNO/10TnnhSxtUdW8bbGf6QiUxiFtUwr648/RQllbIvnmEKwuB2MIx9bLFvONHGFOQzYnLREHhLizCaTxenEQ9D6szTVYzudTgtuK5JzNAl36GepG3xnk3w0aAb83wLTDjMZETpH3cY9hwd2lPeZT79weVqPX6gpP48lvjKzE4QFZWtvPs3sBzCiyaC1w0gHbmOrxnkaRX95JbVtRtqPZMBJMq3DDrr+ls2gYBimaRzCZSD6EI9Bx+RzNxgXZKjZzBUQt2LbKI9uch1xsYJo7ALBqxg3yiMVsnW+UPCxAkVzxYPGqDGh4M1itkqHrPg2Y+v5C0kAXzgPu1joeioX7L24h1/+Szi43+/fc1FhxgjzlYGAP2lgf2AA3UADY3ceDvhp4KlJUbyz1xydVNMUDewPDN9I4cLhhRE/BVwzHWz8+OmolvW2f2opYP/ITRSwHVewnwaWryi+FrIREE3NNLD/ZrpLX6CCcS7jl4gmuskOwSUVjeM0VPAtFDB+9zwNDKe4NK8aC4Kdogoa+FY6eJAGhqUWRm5nJ07jpYFvmwH/L8GsU9GYpNCAJF+3U8DCmf3kSmHCziVdd+hmNKMZJV7/AkyiK5JxlnL4AAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDIxLTAzLTA0VDIwOjUwOjQ0KzAwOjAwjO+KGgAAACV0RVh0ZGF0ZTptb2RpZnkAMjAyMS0wMy0wNFQyMDo1MDo0NCswMDowMP2yMqYAAAAASUVORK5CYII=\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";
}

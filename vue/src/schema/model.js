let TEMPLATE_JSON = {
    ID: '', // 模板Id
    NAME: '',
    TITLE: '我的简历2025',
    LIST: [
        {
            id: '1',
            iconfont: 'icon-wodejianli',
            model: 'RESUME_TITLE',
            show: true,
            title: '我的简历'
        },
        {
            id: '2',
            iconfont: 'icon-jibenziliao',
            model: 'BASE_INFO',
            show: true,
            title: '基本资料',
            name: '小猪课堂',
            age: 25,
            address: '四川成都',
            avatar: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
            workService: 3,
            phoneNumber: '028-1234321',
            email: '12322233@qq.com',
            abstract: '我是一个没有感情的工作机器',
            degree: '本科',
            isShow: {
                age: true,
                address: true,
                avatar: true,
                workService: true,
                phoneNumber: true,
                email: true,
                abstract: true,
                degree: true
            }
        },
        {
            id: '3',
            iconfont: 'icon-yixianggangwei',
            model: 'JOB_INTENTION',
            show: true,
            title: '求职意向',
            intendedPositions: '前端开发工程师',
            intendedCity: '成都',
            expectSalary: '8000-10000',
            jobStatus: '随时入职',
            jobSearchType: '求职类型',
            isShow: {
                intendedPositions: true,
                intendedCity: true,
                expectSalary: true,
                jobStatus: true,
                jobSearchType: true
            }
        },
        {
            id: '4',
            iconfont: 'icon-education',
            model: 'EDU_BACKGROUND',
            show: true,
            title: '教育背景',
            LIST: [
                {
                    date: ['2015-5', '2019-6'],
                    schoolName: '野鸡大学',
                    specialized: '通信工程',
                    degree: '本科',
                    majorCourse: '主修计算机技术、Java、c++等等'
                },
                {
                    date: ['2015-5', '2019-6'],
                    schoolName: '野鸡大学',
                    specialized: '通信工程',
                    degree: '本科',
                    majorCourse: '主修计算机技术、Java、c++等等'
                }
            ],
            isShow: {
                date: true,
                schoolName: true,
                specialized: true,
                degree: true,
                majorCourse: true
            }
        },
        {
            id: '5',
            iconfont: 'icon-jinengtechang',
            model: 'SKILL_SPECIALTIES',
            show: true,
            title: '技能特长',
            LIST: [
                {
                    skillName: 'JavaScript',
                    proficiency: '了解',
                    introduce: '熟练掌握该项技术'
                },
                {
                    skillName: 'JavaScript',
                    proficiency: '一般',
                    introduce: '熟练掌握该项技术熟练掌握该项技术熟练掌握该项技术熟练掌握该项技术'
                },
                {
                    skillName: 'JavaScript',
                    proficiency: '熟悉',
                    introduce: '熟练掌握该项技术熟练掌握该项技术熟练掌握该项技术熟练掌握该项技术'
                },
                {
                    skillName: 'JavaScript',
                    proficiency: '精通',
                    introduce: '熟练掌握该项技术'
                }
            ]
        },
        {
            id: '6',
            iconfont: 'icon-xiaoyuanjingli',
            model: 'CAMPUS_EXPERIENCE',
            show: true,
            title: '校园经历',
            LIST: [
                {
                    date: ['2021-9', '2022-10'],
                    campusBriefly: '经历简要，如社团名称',
                    campusDuty: '主要职责',
                    campusContent: '经历描述'
                },
                {
                    date: ['2021-9', '2022-10'],
                    campusBriefly: '经历简要，如社团名称',
                    campusDuty: '主要职责',
                    campusContent: '经历经历描述经历描述经历描经历描述经历描述经历描述经历描述经历描述述经历描述经历描述经历描述经历描述经历描述描述'
                }
            ],
            isShow: {
                date: true,
                campusBriefly: true,
                campusDuty: true,
                campusContent: true
            }
        },
        {
            id: '9',
            iconfont: 'icon-shixi',
            model: 'INTERNSHIP_EXPERIENCE',
            show: true,
            title: '实习经验',
            LIST: [
                {
                    date: ['2021-9', '2022-10'],
                    companyName: '业绩公司',
                    posts: '前端开发工程师',
                    jobContent: [
                        { content: '主要工作内容是打杂' },
                        { content: '主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂' },
                        { content: '主要工作内容是打杂' }
                    ]
                },
                {
                    date: ['2021-9', '2022-10'],
                    companyName: '业绩公司',
                    posts: '前端开发工程师',
                    jobContent: [
                        { content: '主要工作内容是打杂' },
                        { content: '主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂主要工作内容是打杂' },
                        { content: '主要工作内容是打杂' }
                    ]
                }
            ],
            isShow: {
                date: true,
                companyName: true,
                posts: true,
                jobContent: true
            }
        },
        {
            id: '7',
            iconfont: 'icon-gongzuojingyan',
            model: 'WORK_EXPERIENCE',
            show: true,
            title: '工作经验',
            LIST: [
                {
                    date: ['2021-9', '2022-10'],
                    companyName: '业绩公司',
                    posts: '前端开发工程师',
                    jobContent: [
                        { content: '主要工作内容是打杂' }
                    ]
                }
            ],
            isShow: {
                date: true,
                companyName: true,
                posts: true
            }
        },
        {
            id: '8',
            iconfont: 'icon-xiangmu',
            model: 'PROJECT_EXPERIENCE',
            show: true,
            title: '项目经验',
            LIST: [
                {
                    date: ['2021-9', '2022-10'],
                    projectName: '项目名称',
                    posts: '前端开发工程师',
                    projectContent: [
                        { content: '该项目是一个打杂项目' }
                    ]
                }
            ],
            isShow: {
                date: true,
                projectName: true,
                posts: true
            }
        },
        {
            id: '10',
            iconfont: 'icon-rongyu',
            model: 'AWARDS',
            show: true,
            title: '荣誉奖项',
            LIST: [
                {
                    date: '2021-9',
                    awardsName: '奖项名称',
                    awardsGrade: '获奖等级'
                }
            ],
            isShow: {
                date: true,
                awardsName: true,
                awardsGrade: true
            }
        },
        {
            id: '11',
            iconfont: 'icon-xingquaihao',
            model: 'HOBBIES',
            show: true,
            title: '兴趣爱好',
            content: '我喜欢打杂我喜欢打杂我喜欢打杂我喜欢打杂我喜欢打杂我喜欢打杂我喜欢打杂我喜欢打杂',
            style: {
                textColor: '#757575',
                textFontSize: '14px',
                textFontWeight: 500,
                mBottom: '45px',
                mTop: '0px'
            }
        },
        {
            id: '12',
            iconfont: 'icon-ziwopingjia',
            model: 'SELF_EVALUATION',
            show: true,
            title: '自我评价',
            content: '我是一个没有责任感的人'
        },
        {
            id: '13',
            iconfont: 'icon-zhuanyezhishijineng',
            model: 'WORKS_DISPLAY',
            show: true,
            title: '作品展示',
            LIST: [
                {
                    worksName: '作品名称',
                    worksLink: 'https://www.baidu.com',
                    worksIntroduce: '这是一个千万级作品'
                },
                {
                    worksName: '作品名称',
                    worksLink: 'https://www.baidu.com',
                    worksIntroduce: '这是一个千万级作品'
                }
            ]
        }
    ],
    GLOBAL_STYLE: {
        themeColor: '#079cfa',      // 主题颜色
        firstTitleFontSize: '20px', // 一级标题
        secondTitleFontSize: '14px',// 二级标题
        textFontSize: '14px',       // 文本字体大小
        secondTitleColor: '#666',   // 二级标题颜色
        textFontColor: '#757575',   // 文本字体颜色
        secondTitleWeight: 600,     // 二级标题粗细
        textFontWeight: 500,        // 文本字体粗细
        modelMarginTop: '0px',      // 模版上边距
        modelMarginBottom: '45px'   // 模版底边距
    }
};

export default TEMPLATE_JSON;
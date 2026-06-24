import styles from '@/schema/style';
import { cloneDeep } from 'lodash';

/**
 * @description 给每个模块添加基础样式
 * @param {Object} resumeJson - 简历 JSON 数据，必须包含 NAME 字段
 */
const useAddStyle = (resumeJson) => {
    let templateName = resumeJson.NAME || 'template1';
    let deepStyles = cloneDeep(styles);
    // 判断每一模块是否有style属性，没有则加上
    let temp = JSON.parse(JSON.stringify(resumeJson));
    temp.LIST.forEach((item, index) => {
        if (!item.style && deepStyles[templateName] && deepStyles[templateName][item.model]) {
            item.style = deepStyles[templateName][item.model];
        }
        temp.LIST[index] = item;
    });
    if (deepStyles[templateName]) {
        temp.GLOBAL_STYLE = deepStyles[templateName]['GLOBAL_STYLE']; // 全局样式
    }
    return temp;
};
export default useAddStyle;

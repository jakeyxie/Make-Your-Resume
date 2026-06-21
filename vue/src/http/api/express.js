import axios from 'axios';

const API_BASE_URL = 'https://api.deepseek.com';
export const getAIResponse = async (userQuestion) => {
    console.log('API密钥状态:', import.meta.env.VITE_API_KEY ? '已加载' : '未配置');
    try {
        const response = await axios.post(
            `${API_BASE_URL}/chat/completions`,
            {
                model: "deepseek-chat",
                messages: [
                    {
                        role: "system",
                        content: "你是一个专业的技术助手，需要回答用户提交的网页相关问题"
                    },
                    {
                        role: "user",
                        content: userQuestion
                    }
                ],
                stream: false
            },
            {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${import.meta.env.VITE_API_KEY}`
                }
            }
        );

        return response.data.choices[0].message.content;
    } catch (error) {
        console.error("API调用失败:", error.response?.data || error.message);
        throw new Error("获取回答失败，请稍后重试");
    }
};


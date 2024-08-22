import { ref } from 'vue';
import api from '../services/api';

export const useUserStore = () => {
    const user = ref(null);

    const decodeToken = (token) => {
        try {
            const base64Url = token.split(".")[1];
            const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
            const jsonPayload = decodeURIComponent(
                atob(base64)
                    .split("")
                    .map(c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
                    .join("")
            );
            const decoded = JSON.parse(jsonPayload);
            console.log('Decoded token:', decoded);
            return decoded;
        } catch (error) {
            console.error('Error decoding token:', error);
            return {};
        }
    };

    const fetchUser = () => {
        const token = localStorage.getItem("token");
        if (token) {
            user.value = decodeToken(token);
            console.log('Fetched user:', user.value); // Добавьте это для отладки
        } else {
            user.value = null;
        }
    };
    const login = async (email, password) => {
        try {
            const response = await api.post('/user/login', null, {
                params: { email, password },
            });
            const token = response.data.token;
            localStorage.setItem('token', token);
            fetchUser();
        } catch (error) {
            throw new Error('Ошибка входа: ' + (error.response?.data || 'Неизвестная ошибка'));
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        user.value = null;
    };

    return { user, fetchUser, login, logout };
};
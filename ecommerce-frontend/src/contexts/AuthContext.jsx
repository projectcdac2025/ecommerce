import React, { createContext, useEffect, useState} from 'react';
import api from '../services/api';
 export const AuthContext = createContext();
 export const AuthProvider = ({ children }) => {
     const [user, setUser] = useState(null);
     useEffect(() => {
         const storedUser = localStorage.getItem('user');
            if (storedUser) {   
                setUser(JSON.parse(storedUser));
            }
        }, []);
        const login = async (credentials) => {
            const response = await api.post('/auth/login', credentials);
            const {token,user} = response.data;
            
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('token', token);
            setUser(user);
        };
        const logout = () => {
            localStorage.removeItem('user');
            localStorage.removeItem('token');
            setUser(null);
        };
const signup = async (payload) => {
    return  await api.post('/auth/signup', payload);
    
};
     return (
         <AuthContext.Provider value={{ user, login, logout, signup }}>
             {children}
         </AuthContext.Provider>
     );
 }  ;
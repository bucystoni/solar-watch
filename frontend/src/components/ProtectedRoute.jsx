import {useAuth} from "../context/useAuth.js";
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {
    const { token } = useAuth();

    if (!token) return <Navigate to="/login" replace />;
    return children;
}
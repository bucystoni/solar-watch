import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {useAuth} from "../context/useAuth.js";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    async function handleSubmit(e) {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        try {
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                if (response.status === 401) {
                    setError("Incorrect username or password.");
                } else {
                    setError("Login failed. Please try again.");
                }
                return;
            }

            const data = await response.json();
            login(data.token)
            navigate("/solar-watch", { replace: true });
        } catch {
            setError("Cannot reach the server. Is the backend running?");
        } finally {
            setSubmitting(false);
        }
    }

    return (<div>
        <h1>Login Page</h1>
        {error && <p role={"alert"}>{error}</p>}
        <form onSubmit={handleSubmit}>
            <label htmlFor={"username"}>Username</label>
            <input id={"username"} value={username}
                onChange={(e) => setUsername(e.target.value)} />
            <label htmlFor={"password"}>Password</label>
            <input id={"password"} value={password} type="password"
                onChange={(e) => setPassword(e.target.value)} />
            <button type="submit" disabled={submitting}>Login</button>
        </form>
    </div>)
}
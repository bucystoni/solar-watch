import {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({username, password})
            });

            if (!response.ok) {
                const message = await response.text();
                setError(message || "Registration failed.");
                return;
            }

            navigate("/login", { replace: true });
        } catch {
            setError("Cannot reach the server. Is the backend running?");
        } finally {
            setSubmitting(false);
        }
    }

    return (<div>
    <h1>RegisterPage</h1>
        {error && <p role={"alert"}>{error}</p>}
        <form onSubmit={handleSubmit}>
            <label htmlFor={"username"}>Username</label>
            <input id={"username"} value={username}
                   onChange={(e) => setUsername(e.target.value)}/>
            <label htmlFor={"password"}>Password</label>
            <input id={"password"} value={password} type="password"
                   onChange={(e) => setPassword(e.target.value)}/>
            <button type="submit" disabled={submitting}>Register</button>
        </form>
    </div>)
}
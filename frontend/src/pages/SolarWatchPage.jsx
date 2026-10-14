import { useState } from "react"
import {useAuth} from "../context/useAuth.js";
import SolarTime from "../components/SolarTime";

export default function SolarWatchPage() {
    const [city, setCity] = useState("");
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const { token } = useAuth();

    async function handleSubmit(e) {
        e.preventDefault();
        setResult(null);
        setError(null);
        setLoading(true);

        try {
            const response = await fetch(`http://localhost:8080/api/solardata?city=${encodeURIComponent(city)}`, {
                method: "GET",
                headers: { Authorization: `Bearer ${token}` }
            }
            )

            if (!response.ok) {
                if (response.status === 404) {
                    setError(await response.text());
                    return;
                } else if (response.status === 401) {
                    setError("Your session expired. Please login again.");
                    return;
                } else {
                    setError("Something went wrong.")
                    return;
                }
            }

            setResult(await response.json())

        } catch {
            setError("Cannot reach the server. Is the backend running?");
        } finally {
            setLoading(false);
        }

    }

    return (<div>
        <h1>SolarWatch</h1>
        {error && <p role="alert">{error}</p>}
        <form onSubmit={handleSubmit}>
            <label htmlFor="city">Choose a city</label>
            <input id="city" value={city} onChange={(e) => setCity(e.target.value)} />
            <button type="submit" disabled={loading}>
                {loading ? "Loading…" : "Show solar data"}
            </button>
        </form>
        {result && <SolarTime result={result}></SolarTime>}
    </div>)
}
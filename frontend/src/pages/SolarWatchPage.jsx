import {useState} from "react"
import {useAuth} from "../context/useAuth.js";
import SolarTime from "../components/SolarTime";

export default function SolarWatchPage() {
    const [city, setCity] = useState("");
    const [date, setDate] = useState("");
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const {token} = useAuth();

    async function handleSubmit(e) {
        e.preventDefault();
        setResult(null);
        setError(null);
        setLoading(true);

        try {
            const params = new URLSearchParams({city});
            if (date) params.set("date", date);

            const response = await fetch(`http://localhost:8080/api/solardata?${params}`, {
                    method: "GET",
                    headers: {Authorization: `Bearer ${token}`}
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
        {error && <p role="alert">{error}</p>}
        <form onSubmit={handleSubmit} className="search">
            <div className="field">
                <label htmlFor="city">City</label>
                <input id="city" value={city} onChange={(e) => setCity(e.target.value)}/>
            </div>
            <div className="field">
                <label htmlFor="date">Date</label>
                <input id="date" type="date" value={date} onChange={(e) => setDate(e.target.value)}/>
            </div>
            <button type="submit" disabled={loading}>
                {loading ? "Loading…" : "Show sun times"}
            </button>
        </form>
        {result && <SolarTime result={result}></SolarTime>}
    </div>)
}
export default function SolarTime({ result }) {

    return (
        <div>
            <h2>{result.city.name + ", " + result.city.country}</h2>
            <p>Sunrise: {new Date(result.sunrise).toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" })}</p>
            <p>Sunset: {new Date(result.sunset).toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit" })}</p>
            <p>{result.date}</p>
        </div>
    )
}
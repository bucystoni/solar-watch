function formatTime(iso, timeZone) {
    return new Date(iso).toLocaleTimeString("en-GB", { hour: "2-digit", minute: "2-digit", timeZone });
}

function minutesSinceMidnight(iso, timeZone) {
    const time = formatTime(iso, timeZone);      // "05:47", in the city's timezone
    const [hours, minutes] = time.split(":");    // ["05", "47"]
    return Number(hours) * 60 + Number(minutes); // 5 * 60 + 47 = 347
}

function formatDate(isoDate) {
    return new Date(isoDate).toLocaleDateString("en-GB", {
        weekday: "long", day: "numeric", month: "long", timeZone: "UTC",
    });
}

export default function SolarTime({ result }) {
    const zone = result.city.timezone ?? undefined;
    const sunriseMin = minutesSinceMidnight(result.sunrise, zone);
    const sunsetMin = minutesSinceMidnight(result.sunset, zone);
    const dayLength = sunsetMin - sunriseMin;

    return (
        <section className="result">
            <h2>{result.city.name}, {result.city.country}</h2>
            <p className="result-date">{formatDate(result.date)}</p>

            <div className="times">
                <div>
                    <p className="time">{formatTime(result.sunrise, zone)}</p>
                    <p className="time-label">Sunrise</p>
                </div>
                <div className="sunset">
                    <p className="time">{formatTime(result.sunset, zone)}</p>
                    <p className="time-label">Sunset</p>
                </div>
            </div>

            <div className="day-bar" aria-hidden="true">
                <div
                    className="daylight"
                    style={{
                        left: `${(sunriseMin / 1440) * 100}%`,
                        width: `${(dayLength / 1440) * 100}%`,
                    }}
                />
            </div>
            <p className="day-length">
                {Math.floor(dayLength / 60)} h {dayLength % 60} min of daylight
            </p>
        </section>
    );
}
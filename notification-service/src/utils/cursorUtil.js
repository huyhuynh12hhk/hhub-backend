function encodeCursor({ lastCreatedAt, lastId }) {
	const payload = {
		t: lastCreatedAt.toISOString(),
		i: lastId.toString(),
	};
	const str = JSON.stringify(payload);
	const b64Str = Buffer.from(str).toString("base64")
	console.log("B64: ", b64Str)
	return b64Str.replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

const decodeCursor = (cursorStr) => {
	const padLen = (4 - (cursorStr.length % 4)) % 4;
	const b64 = cursorStr.replace(/-/g, "+").replace(/_/g, "/") + "=".repeat(padLen);

	// 2) Base64-decode and parse JSON
	const json = Buffer.from(b64, "base64").toString("utf8");
	const { t, i } = JSON.parse(json);

	// 3) Return typed values
	return {
		lastCreatedAt: new Date(t),
		lastId: new Types.ObjectId(i),
	};
};

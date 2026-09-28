const API = "";

async function checkHealth() {
    try {
        const response = await fetch("/actuator/health");

        if (response.ok) {
            document.getElementById("health").textContent = "System Online";
            document.getElementById("health").style.background = "#dcfce7";
            document.getElementById("health").style.color = "#166534";
        } else {
            throw new Error();
        }
    } catch {
        document.getElementById("health").textContent = "System Offline";
        document.getElementById("health").style.background = "#fee2e2";
        document.getElementById("health").style.color = "#991b1b";
    }
}


async function createBucket() {
    const name = document.getElementById("bucketName").value.trim();

    if (!name) {
        alert("Enter a bucket name.");
        return;
    }

    try {
        const response = await fetch(
            `/buckets?name=${encodeURIComponent(name)}`,
            {
                method: "POST"
            }
        );

        const text = await response.text();

        if (!response.ok) {
            throw new Error(text);
        }

        document.getElementById("bucketMessage").textContent =
            "Bucket created successfully.";

        document.getElementById("bucketName").value = "";

        loadBuckets();

    } catch (error) {
        document.getElementById("bucketMessage").textContent =
            "Error: " + error.message;
    }
}


async function loadBuckets() {
    try {
        const response = await fetch("/buckets?ts=" + Date.now());
        const buckets = await response.json();

        const select = document.getElementById("bucketSelect");

        select.innerHTML =
            '<option value="">Select a bucket</option>';

        buckets.forEach(bucket => {
            const option = document.createElement("option");

            option.value = bucket.name;
            option.textContent = bucket.name;

            select.appendChild(option);
        });

    } catch (error) {
        alert("Failed to load buckets.");
    }
}


async function uploadObject() {
    const bucket = document.getElementById("bucketSelect").value;
    const objectName =
        document.getElementById("objectName").value.trim();

    const file =
        document.getElementById("fileInput").files[0];

    if (!bucket) {
        alert("Select a bucket.");
        return;
    }

    if (!file) {
        alert("Select a file.");
        return;
    }

    const name = objectName || file.name;

    try {
        const response = await fetch(
            `/buckets/${encodeURIComponent(bucket)}/objects/${encodeURIComponent(name)}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/octet-stream"
                },
                body: file
            }
        );

        const result = await response.json();

        if (!response.ok) {
            throw new Error(JSON.stringify(result));
        }

        document.getElementById("uploadMessage").textContent =
            `Uploaded successfully. Version ${result.version}`;

        document.getElementById("objectName").value = "";
        document.getElementById("fileInput").value = "";

        loadObjects();

    } catch (error) {
        document.getElementById("uploadMessage").textContent =
            "Upload failed: " + error.message;
    }
}


async function loadObjects() {
    const bucket =
        document.getElementById("bucketSelect").value;

    if (!bucket) {
        document.getElementById("objects").innerHTML =
            '<div class="empty">Select a bucket.</div>';
        return;
    }

    try {
        const response = await fetch(
            `/buckets/${encodeURIComponent(bucket)}/objects`
        );

        const objects = await response.json();

        const container = document.getElementById("objects");

        container.innerHTML = "";

        if (objects.length === 0) {
            container.innerHTML =
                '<div class="empty">No objects in this bucket.</div>';
            return;
        }

        objects.forEach(object => {

            const div = document.createElement("div");

            div.className = "object";

            div.innerHTML = `
                <div class="object-name">
                    ${escapeHtml(object.objectName)}
                </div>

                <div class="object-info">
                    Version: ${object.version}
                    &nbsp; | &nbsp;
                    Size: ${object.size} bytes
                </div>

                <button onclick="downloadObject('${escapeAttr(object.objectName)}')">
                    Download
                </button>

                <button onclick="showVersions('${escapeAttr(object.objectName)}')">
                    Versions
                </button>

                <button class="delete"
                        onclick="deleteObject('${escapeAttr(object.objectName)}')">
                    Delete
                </button>
            `;

            container.appendChild(div);
        });

    } catch (error) {
        document.getElementById("objects").innerHTML =
            '<div class="empty">Failed to load objects.</div>';
    }
}


function downloadObject(objectName) {
    const bucket =
        document.getElementById("bucketSelect").value;

    window.open(
        `/buckets/${encodeURIComponent(bucket)}/objects/${encodeURIComponent(objectName)}`,
        "_blank"
    );
}


async function showVersions(objectName) {
    const bucket =
        document.getElementById("bucketSelect").value;

    try {
        const response = await fetch(
            `/buckets/${encodeURIComponent(bucket)}/objects/${encodeURIComponent(objectName)}/versions`
        );

        const versions = await response.json();

        let message = `${objectName} versions:\n\n`;

        versions.forEach(version => {
            message +=
                `Version ${version.version} | ` +
                `${version.size} bytes | ` +
                `${version.createdAt}\n`;
        });

        alert(message);

    } catch {
        alert("Failed to load versions.");
    }
}


async function deleteObject(objectName) {
    const bucket =
        document.getElementById("bucketSelect").value;

    if (!confirm(`Delete ${objectName}?`)) {
        return;
    }

    try {
        const response = await fetch(
            `/buckets/${encodeURIComponent(bucket)}/objects/${encodeURIComponent(objectName)}`,
            {
                method: "DELETE"
            }
        );

        if (!response.ok) {
            throw new Error();
        }

        loadObjects();

    } catch {
        alert("Failed to delete object.");
    }
}


function escapeHtml(value) {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


function escapeAttr(value) {
    return value.replaceAll("'", "\\'");
}


checkHealth();
loadBuckets();
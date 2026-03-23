#!/usr/bin/with-contenv bash
# Runs after jitsi-web init — patches the generated config.js for local dev.
CONFIG=/config/config.js
if [ -f "$CONFIG" ]; then
    # Disable pre-join screen (new API: prejoinConfig.enabled)
    sed -i 's/prejoinConfig = {/prejoinConfig = { \/\* patched \*\//g' "$CONFIG"
    sed -i '/\/\* patched \*\//,/^}/{ s/enabled: true/enabled: false/g }' "$CONFIG"

    # Append overrides at the end to be safe
    cat >> "$CONFIG" <<'EOF'

// ── local-dev overrides ──────────────────────────────────────────────────────
config.prejoinConfig = { enabled: false, hideDisplayName: true };
config.prejoinPageEnabled = false;
// ────────────────────────────────────────────────────────────────────────────
EOF
    echo "[patch-config] Applied local-dev patches to $CONFIG"
fi

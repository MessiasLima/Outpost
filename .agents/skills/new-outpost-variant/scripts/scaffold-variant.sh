#!/bin/sh
#
# Scaffolds a new Outpost variant (its android, desktop, shared and ios modules) from the
# templates in this skill. Read ../SKILL.md first: it documents the workflow, the placeholders
# and the HARD RULES that apply to the generated modules.
#
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
SKILL_DIR=$(CDPATH= cd -- "$SCRIPT_DIR/.." && pwd)
ROOT=$(CDPATH= cd -- "$SKILL_DIR/../../.." && pwd)
TEMPLATES="$SKILL_DIR/templates"
MANIFEST="$TEMPLATES/manifest.txt"
SETTINGS="$ROOT/settings.gradle.kts"
SETTINGS_ANCHOR='include(":outpost-core")'
REFERENCE_NAME='kotlin-outpost'
PLATFORM_ORDER='android desktop ios'

usage() {
    cat <<'EOF'
Usage: scaffold-variant.sh <variant-name> [options]

  <variant-name>          directory and Gradle path of the new variant: kebab-case, lowercase
                          alphanumeric with single hyphens (e.g. rust-outpost, mycontent).
  --display-name "Name"   human readable app name (default: the variant name in Title Case).
  --platforms <list>      comma separated app modules to generate: android, desktop, ios
                          (default: all three). The shared bridge is always generated, because
                          every platform entry point goes through it.
  --package <suffix>      Kotlin package suffix, for names that do not imply one themselves
                          (default: the variant name without -outpost and without hyphens, so
                          rust-outpost -> rust). Dots are allowed: community.rust.
  --dry-run               print the derived identifiers and the files that would be written.

The script resolves the repository root from its own location, creates <root>/<variant-name>/ and
adds the selected modules to settings.gradle.kts. It refuses to touch an existing variant, never
overwrites a file, and writes nothing on --dry-run.
EOF
}

die() {
    printf 'scaffold-variant: %s\n' "$*" >&2
    exit 1
}

trim() {
    printf '%s' "$1" | sed 's/^[[:space:]]*//; s/[[:space:]]*$//'
}

upper_first() {
    printf '%s' "$1" | cut -c1 | tr '[:lower:]' '[:upper:]'
}

lower_first() {
    printf '%s%s' "$(printf '%s' "$1" | cut -c1 | tr '[:upper:]' '[:lower:]')" "$(rest_of "$1")"
}

rest_of() {
    printf '%s' "$1" | cut -c2-
}

# rust-outpost -> RustOutpost
pascal() {
    pascal_result=''
    for pascal_word in $(printf '%s' "$1" | sed 's/-/ /g'); do
        pascal_result="$pascal_result$(upper_first "$pascal_word")$(rest_of "$pascal_word")"
    done
    printf '%s' "$pascal_result"
}

# rust-outpost -> Rust Outpost
title_case() {
    title_result=''
    for title_word in $(printf '%s' "$1" | sed 's/-/ /g'); do
        if [ -n "$title_result" ]; then
            title_result="$title_result "
        fi
        title_result="$title_result$(upper_first "$title_word")$(rest_of "$title_word")"
    done
    printf '%s' "$title_result"
}

# True when a module of the manifest is generated: shared always is, the platform modules when
# they are listed in --platforms.
is_selected() {
    if [ "$1" = 'shared' ]; then
        return 0
    fi
    case ",$SELECTED," in
    *",$1,"*) return 0 ;;
    esac
    return 1
}

# Renders stdin to stdout. $1 is "template" (placeholders only) or "reference"
# (placeholders plus the token rewrites that turn the kotlin-outpost files into the
# new variant's files).
render() {
    template_replacements() {
        sed \
            -e "s|{{NAME}}|$NAME|g" \
            -e "s|{{DISPLAY}}|$DISPLAY|g" \
            -e "s|{{PKG_PATH}}|$PKG_PATH|g" \
            -e "s|{{PKG}}|$PKG|g" \
            -e "s|{{SYMBOL}}|$SYMBOL|g" \
            -e "s|{{CAMEL}}|$CAMEL|g" \
            -e "s|{{FRAMEWORK}}|$FRAMEWORK|g" \
            -e "s|{{RES_PKG}}|$RES_PKG|g" \
            -e "s|{{BUNDLE_ID}}|$BUNDLE_ID|g"
    }

    case "$1" in
    template)
        template_replacements
        ;;
    reference)
        sed \
            -e "s|KotlinOutpostShared|$FRAMEWORK|g" \
            -e "s|Kotlin Outpost|$DISPLAY|g" \
            -e "s|$REFERENCE_NAME|$NAME|g" \
            -e "s|dev.appoutlet.outpost.Kotlin|$BUNDLE_ID|g" \
            | template_replacements
        ;;
    *)
        die "unknown render mode '$1'"
        ;;
    esac
}

NAME=''
DISPLAY=''
PACKAGE=''
PLATFORMS=''
DRY_RUN=0

while [ $# -gt 0 ]; do
    case "$1" in
    --display-name)
        [ $# -ge 2 ] || die "--display-name needs a value"
        DISPLAY="$2"
        shift 2
        ;;
    --platforms)
        [ $# -ge 2 ] || die "--platforms needs a value"
        PLATFORMS="$2"
        shift 2
        ;;
    --package)
        [ $# -ge 2 ] || die "--package needs a value"
        PACKAGE="$2"
        shift 2
        ;;
    --dry-run)
        DRY_RUN=1
        shift
        ;;
    -h | --help)
        usage
        exit 0
        ;;
    -*)
        printf 'scaffold-variant: unknown option %s\n\n' "$1" >&2
        usage >&2
        exit 2
        ;;
    *)
        [ -z "$NAME" ] || die "only one variant name can be scaffolded at a time"
        NAME="$1"
        shift
        ;;
    esac
done

[ -n "$NAME" ] || {
    usage >&2
    exit 2
}

case "$NAME" in
*[!a-z0-9-]*) die "variant name '$NAME' must be lowercase alphanumeric with hyphens" ;;
-* | *-) die "variant name '$NAME' must not start or end with a hyphen" ;;
*--*) die "variant name '$NAME' must not contain consecutive hyphens" ;;
esac

[ -f "$MANIFEST" ] || die "missing manifest $MANIFEST"
[ -f "$SETTINGS" ] || die "$SETTINGS not found: run the script from inside the Outpost repository"
[ -d "$ROOT/$REFERENCE_NAME" ] || die "$ROOT/$REFERENCE_NAME not found: the reference variant is required"

if [ -z "$PLATFORMS" ]; then
    PLATFORMS='android,desktop,ios'
fi
PLATFORMS=$(printf '%s' "$PLATFORMS" | sed 's/[[:space:]]//g')
for platform in $(printf '%s' "$PLATFORMS" | sed 's/,/ /g'); do
    case " $PLATFORM_ORDER " in
    *" $platform "*) ;;
    *) die "--platforms accepts android, desktop and ios (the shared bridge is always generated), got '$platform'" ;;
    esac
done
SELECTED=''
for platform in $PLATFORM_ORDER; do
    case ",$PLATFORMS," in
    *",$platform,"*)
        if [ -n "$SELECTED" ]; then
            SELECTED="$SELECTED,"
        fi
        SELECTED="$SELECTED$platform"
        ;;
    esac
done
[ -n "$SELECTED" ] || die "--platforms needs at least one of android, desktop, ios"

BASE="$NAME"
case "$NAME" in
*-outpost) BASE="${NAME%-outpost}" ;;
esac
PKG=$(printf '%s' "$BASE" | sed 's/-//g')
if [ -n "$PACKAGE" ]; then
    PACKAGE=$(printf '%s' "$PACKAGE" | sed 's/^\.*//; s/\.*$//')
    printf '%s' "$PACKAGE" | grep -qE '^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$' ||
        die "--package must be a lowercase Kotlin package suffix, such as 'rust' or 'community.rust'"
    PKG="$PACKAGE"
fi
SYMBOL=$(pascal "$NAME")
CAMEL="$(lower_first "$SYMBOL")"
PKG_PATH=$(printf '%s' "$PKG" | sed 's/\./\//g')
FRAMEWORK="${SYMBOL}Shared"
RES_PKG="outpost.$(printf '%s' "$NAME" | sed 's/-/_/g').shared.generated.resources"
BUNDLE_ID="dev.appoutlet.outpost.$(pascal "${PKG##*.}")"

if [ -z "$DISPLAY" ]; then
    DISPLAY=$(title_case "$NAME")
else
    DISPLAY=$(trim "$DISPLAY")
fi

[ -n "$DISPLAY" ] || die "--display-name must not be empty"
printf '%s' "$DISPLAY" | grep -q '^[A-Za-z0-9.+ -]*$' ||
    die "--display-name may only contain letters, digits, spaces, dots, plus signs and hyphens"

[ -e "$ROOT/$NAME" ] && die "$NAME already exists at $ROOT/$NAME"
grep -qF "include(\":$NAME:" "$SETTINGS" && die "$NAME is already included in settings.gradle.kts"
grep -qF "$SETTINGS_ANCHOR" "$SETTINGS" ||
    die "settings.gradle.kts has no $SETTINGS_ANCHOR line, so the modules cannot be wired in"

PKG_CLASH=$(find "$ROOT" -name build.gradle.kts -not -path '*/build/*' -not -path '*/.git/*' \
    -exec grep -l "dev\.appoutlet\.outpost\.$PKG\"" {} + 2>/dev/null || true)
[ -z "$PKG_CLASH" ] || die "package dev.appoutlet.outpost.$PKG is already used by $PKG_CLASH"

printf 'Scaffolding %s (%s)\n\n' "$NAME" "$DISPLAY"
printf '  %-12s %s\n' \
    'variant' "$NAME" \
    'display' "$DISPLAY" \
    'modules' "shared, $SELECTED" \
    'package' "dev.appoutlet.outpost.$PKG" \
    'symbol' "$SYMBOL" \
    'framework' "$FRAMEWORK" \
    'resources' "$RES_PKG" \
    'bundle id' "$BUNDLE_ID"
printf '\n'

FILES_WRITTEN=0
while IFS='|' read -r MODULE SOURCE TARGET; do
    MODULE=$(trim "$MODULE")
    SOURCE=$(trim "$SOURCE")
    TARGET=$(trim "$TARGET")

    case "$MODULE" in
    '' | '#'*) continue ;;
    esac
    case " android desktop ios shared " in
    *" $MODULE "*) ;;
    *) die "manifest module '$MODULE' is not one of android, desktop, ios, shared" ;;
    esac
    is_selected "$MODULE" || continue

    case "$SOURCE" in
    templates/*) MODE='template'; SOURCE_FILE="$SKILL_DIR/$SOURCE" ;;
    repo/*) MODE='reference'; SOURCE_FILE="$ROOT/${SOURCE#repo/}" ;;
    *) die "manifest entry '$SOURCE' must start with templates/ or repo/" ;;
    esac
    [ -f "$SOURCE_FILE" ] || die "manifest source $SOURCE_FILE not found"

    TARGET_PATH="$ROOT/$NAME/$(printf '%s' "$TARGET" | render template)"
    FILES_WRITTEN=$((FILES_WRITTEN + 1))

    if [ "$DRY_RUN" -eq 1 ]; then
        printf '  would write %s\n' "${TARGET_PATH#$ROOT/}"
        continue
    fi

    mkdir -p "$(dirname "$TARGET_PATH")"
    [ -e "$TARGET_PATH" ] && die "$TARGET_PATH already exists, nothing else was written"
    render "$MODE" <"$SOURCE_FILE" >"$TARGET_PATH"
    grep -q '{{' "$TARGET_PATH" &&
        die "$TARGET_PATH still contains an unknown {{PLACEHOLDER}}, check templates/manifest.txt and the templates"
    printf '  wrote %s\n' "${TARGET_PATH#$ROOT/}"
done <"$MANIFEST"

if [ "$DRY_RUN" -eq 1 ]; then
    printf '\nDry run: %s files listed, nothing written. settings.gradle.kts untouched.\n' "$FILES_WRITTEN"
    printf 'It would add to settings.gradle.kts, before %s:\n' "$SETTINGS_ANCHOR"
    for module in android desktop shared; do
        is_selected "$module" && printf '  include(":%s:%s")\n' "$NAME" "$module"
    done
    exit 0
fi

SETTINGS_TMP="$SETTINGS.tmp.$$"
awk -v anchor="$SETTINGS_ANCHOR" -v name="$NAME" -v selected="$SELECTED" '
    function wanted(module) {
        return module == "shared" || index("," selected ",", "," module ",") > 0
    }
    $0 == anchor && !inserted {
        if (wanted("android")) printf "include(\":%s:android\")\n", name
        if (wanted("desktop")) printf "include(\":%s:desktop\")\n", name
        if (wanted("shared")) printf "include(\":%s:shared\")\n", name
        inserted = 1
    }
    { print }
' "$SETTINGS" >"$SETTINGS_TMP"
mv "$SETTINGS_TMP" "$SETTINGS"
printf '  wrote settings.gradle.kts includes for %s\n' "$NAME"

printf '\n%s files created plus the settings.gradle.kts includes.\n' "$FILES_WRITTEN"

cat <<EOF

Next steps
  1. Verify the variant with the focused tasks (never build/assemble/allTests):
EOF
if is_selected android; then
    printf '       ./gradlew :%s:android:compileDebugKotlin :%s:android:processDebugResources\n' "$NAME" "$NAME"
fi
if is_selected desktop; then
    printf '       ./gradlew :%s:desktop:compileKotlin\n' "$NAME"
fi
printf '       ./gradlew :%s:shared:jvmTest\n' "$NAME"
if is_selected ios; then
    printf '       ./gradlew :%s:shared:compileKotlinIosArm64\n' "$NAME"
fi
cat <<EOF
  2. Brand assets are intentionally not scaffolded, so the generated modules carry no
     Kotlin Outpost icons, colours or content: add them before the variant ships.
  3. Point the new app at the shared core through $SYMBOL() only; never fork :outpost-core.
EOF

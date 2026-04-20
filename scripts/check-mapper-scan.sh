#!/bin/bash

# =============================================================================
# MapperScan Configuration Checker Script
# =============================================================================
# Description:
#   This script scans all service modules in the backend project to check
#   whether they have the @MapperScan annotation configured.
#
# Usage:
#   ./check-mapper-scan.sh
#
# Output:
#   - Lists all service modules with @MapperScan configured (OK)
#   - Lists all service modules missing @MapperScan configuration (MISSING)
#
# Note:
#   Run this script from the backend directory or specify the backend path
# =============================================================================

# Set backend directory (default: current directory's parent)
BACKEND_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# If script is run from backend/scripts, go up one level
if [[ "$BACKEND_DIR" == */backend/scripts ]]; then
    BACKEND_DIR="$(dirname "$BACKEND_DIR")"
fi

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=============================================="
echo "  MapperScan Configuration Checker"
echo "=============================================="
echo ""

# Check if service directory exists
if [ ! -d "$BACKEND_DIR/service" ]; then
    echo -e "${RED}Error: service directory not found at $BACKEND_DIR/service${NC}"
    exit 1
fi

# Arrays to store results
declare -a OK_MODULES=()
declare -a MISSING_MODULES=()

# Scan each service module
for service_dir in "$BACKEND_DIR/service"/*/; do
    if [ -d "$service_dir" ]; then
        # Get module name (remove -service suffix for package name)
        module_name=$(basename "$service_dir")
        package_name=$(echo "$module_name" | sed 's/-service$//')

        # Check for @MapperScan in any Java file under config package
        mapper_scan_found=false

        # Find Java files in config directory
        for java_file in "$service_dir/src/main/java/com/example/$package_name/config"/*.java; do
            if [ -f "$java_file" ]; then
                if grep -q "@MapperScan" "$java_file"; then
                    mapper_scan_found=true
                    break
                fi
            fi
        done

        # Check for @MapperScan in Application class as alternative
        if [ "$mapper_scan_found" = false ]; then
            for app_file in "$service_dir/src/main/java/com/example/$package_name"/*Application.java; do
                if [ -f "$app_file" ]; then
                    if grep -q "@MapperScan" "$app_file"; then
                        mapper_scan_found=true
                        break
                    fi
                fi
            done
        fi

        if [ "$mapper_scan_found" = true ]; then
            OK_MODULES+=("$module_name")
        else
            MISSING_MODULES+=("$module_name")
        fi
    fi
done

# Output results
echo "=============================================="
echo "  Check Results"
echo "=============================================="
echo ""

# Print OK modules
echo -e "${GREEN}OK - Modules with @MapperScan configured:${NC}"
if [ ${#OK_MODULES[@]} -eq 0 ]; then
    echo "  (none)"
else
    for module in "${OK_MODULES[@]}"; do
        echo "  [OK] $module"
    done
fi
echo ""

# Print missing modules
echo -e "${RED}MISSING - Modules without @MapperScan:${NC}"
if [ ${#MISSING_MODULES[@]} -eq 0 ]; then
    echo "  (none)"
else
    for module in "${MISSING_MODULES[@]}"; do
        echo "  [MISSING] $module"
    done
    echo ""
    echo -e "${YELLOW}Please add @MapperScan configuration to the following:${NC}"
    for module in "${MISSING_MODULES[@]}"; do
        package_name=$(echo "$module" | sed 's/-service$//')
        echo "  - $BACKEND_DIR/service/$module/src/main/java/com/example/$package_name/config/MyBatisConfig.java"
    done
fi

echo ""
echo "=============================================="
echo "  Summary"
echo "=============================================="
echo -e "Total modules checked: ${GREEN}${#OK_MODULES[@]}${NC} OK, ${RED}${#MISSING_MODULES[@]}${NC} MISSING"
echo ""

# Exit with error if any module is missing @MapperScan
if [ ${#MISSING_MODULES[@]} -gt 0 ]; then
    exit 1
fi

exit 0

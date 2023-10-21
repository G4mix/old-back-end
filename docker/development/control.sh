#!/bin/sh

echo "Escolha uma opção:"
echo "1) dev"
echo "2) unit_test"
echo "3) implementation_test"
echo "4) Sair"

read choice

case $choice in
    1)
        ./scripts/dev.sh
        ;;
    2)
        ./scripts/unit_test.sh
        ;;
    3)
        ./scripts/implementation_test.sh
        ;;
    4)
        exit 0
        ;;
    *)
        echo "Opção inválida. Tente novamente."
        ;;
esac

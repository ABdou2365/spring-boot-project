import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => "+"
const CloseIcon = () => "X"

const CreateCustomerDrawer = ({fetchCustomers}) =>{
    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
            <Button colorScheme='teal' leftIcon={<AddIcon />} onClick={onOpen}>Add Customer</Button>
            <Drawer isOpen={isOpen}  size={"xl"} >
                <DrawerContent>
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm fetchCustomers={fetchCustomers} />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button type='submit' form='my-form' onClick={onClose} leftIcon={CloseIcon}>
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}


export default CreateCustomerDrawer
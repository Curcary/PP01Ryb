using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using The_Testo.Database;

namespace The_Testo.Pages
{
    /// <summary>
    /// Логика взаимодействия для DishesPage.xaml
    /// </summary>
    public partial class DishesPage : Page
    {
        
        public DishesPage( bool isSelection = false)
        {
            InitializeComponent();
            if (isSelection) SelectButton.Visibility = Visibility.Visible;
            GlobalInfo.SelectedDish = null;
        
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            DishView.ItemsSource = DB.entities.Dish.ToList();
            List<string> category_source = DB.entities.Category.Select(s => s.CategoryName).ToList();
            category_source.Insert(0, "Все категории");
            CategoryBox.ItemsSource = category_source;
            CategoryBox.SelectedIndex= 0;
        }

        private void CategoryBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            FilterOut();
        }

        private void SearchBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            FilterOut();
        }
        private void FilterOut()
        {
            DishView.ItemsSource = DB.entities.Dish.Where(s =>
            s.DishName.Contains(SearchBox.Text)
            &&(s.Category.CategoryName==CategoryBox.SelectedItem.ToString()
            || CategoryBox.SelectedItem.ToString()== "Все категории")).ToList();
        }

        private void SelectButton_Click(object sender, RoutedEventArgs e)
        {
            if (DishView.SelectedItem is Dish selectedDish)
            {
                GlobalInfo.SelectedDish = selectedDish;
                NavigationService.GoBack();
            }
        }

        private void DeclineButton_Click(object sender, RoutedEventArgs e)
        {
            GlobalInfo.SelectedDish = null;
            NavigationService.GoBack();
        }

        private void TextBlock_MouseUp(object sender, MouseButtonEventArgs e)
        {
            if (DishView.SelectedItem is Dish selectedDish)
            {
                
            }
        }
    }
}
